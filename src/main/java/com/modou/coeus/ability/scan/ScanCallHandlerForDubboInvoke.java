package com.modou.coeus.ability.scan;

import com.modou.coeus.ability.data.AnnotationForValueData;
import com.modou.coeus.common.ClassRouter;
import com.modou.coeus.common.Constant;
import com.modou.coeus.domain.ClassAndMethodData;
import com.modou.coeus.node.CoeusClassNode;
import com.modou.coeus.node.CoeusMethodNode;
import com.modou.coeus.node.CoeusParamNode;
import com.modou.coeus.utils.ParseUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @program: coeus-x
 * @description: 对于重要接口下游调用接口梳理
 * @author: hu_pf
 * @create: 2023-03-05 17:59
 * doc:
 **/
public class ScanCallHandlerForDubboInvoke extends AbstractScanCallHandler{

    private static Set<String> INTERFACE_NAME = new HashSet<>();

    private LinkedHashSet<String> parseDubboInvoke = new LinkedHashSet<>();

    public ScanCallHandlerForDubboInvoke(String dubboConsumerPath) {
        initPath(dubboConsumerPath);
    }

    @Override
    public void doInvoke(ScanCallHandlerData scanCallHandlerData) {
        CoeusMethodNode invoke = scanCallHandlerData.getCoeusMethodNodeCurrent();
        if (invoke == null || invoke.invokeInfos == null){
            return;
        }

        for (String invokeString : invoke.invokeInfos){
            ClassAndMethodData classAndMethodData = ParseUtils.parseClassAndMethodString(invokeString);
            if (INTERFACE_NAME.contains(classAndMethodData.getClassName())){
                parseDubboInvoke.add(classAndMethodData.getClassName().replaceAll(Constant.CLASS_SPLIT_SLASH,Constant.CLASS_SPLIT_POINT)+"#"+classAndMethodData.getMethodName());
            }
        }

    }

    public Set<String> getParseDubboInvoke(){
        return this.parseDubboInvoke;
    }

    private static void initPath(String dubboConsumerPath){
        try {
            File inputFile = new File(dubboConsumerPath);
            DocumentBuilderFactory dbFactory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("dubbo:reference");
            for (int temp = 0; temp < nodeList.getLength(); temp++) {
                Node node = nodeList.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String serviceName = element.getAttribute("interface");
                    INTERFACE_NAME.add(serviceName.replaceAll(Constant.CLASS_SPLIT_POINT,Constant.CLASS_SPLIT_SLASH));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
