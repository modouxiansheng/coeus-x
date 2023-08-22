package com.modou.coeus.ability.scan;

import com.modou.coeus.common.Constant;
import com.modou.coeus.domain.ClassAndMethodData;
import com.modou.coeus.node.CoeusMethodNode;
import com.modou.coeus.utils.ParseUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;

/**
 * @program: coeus-x
 * @description: 入参对象中哪些参数被这个调用链用到了
 * @author: hu_pf
 * @create: 2023-03-05 17:59
 * doc:
 **/
public class ScanCallHandlerForEntryQuotation extends AbstractScanCallHandler{


    private Map<String, Set<String>> RESULT_PARSE = new HashMap<>();

    private List<String> containString;

    public ScanCallHandlerForEntryQuotation(){
        containString = Collections.singletonList("OrderCreatedMsg");
    }

    public ScanCallHandlerForEntryQuotation(List<String> containString){
        this.containString = containString;
    }

    @Override
    public void doInvoke(ScanCallHandlerData scanCallHandlerData) {
        Set<String> strings = new HashSet<>();
        CoeusMethodNode coeusMethodNodeCurrent = scanCallHandlerData.getCoeusMethodNodePre();
        for (String invokeInfo : coeusMethodNodeCurrent.invokeInfos) {
            if (containString(invokeInfo) && invokeInfo.contains("get")){

                String[] split = invokeInfo.split("#");
                String param = split[1].replaceFirst("get", "").substring(0, 1).toLowerCase() + split[1].substring(4);
                String className = split[0].replaceAll("xxxx","");


                Set<String> orDefault = RESULT_PARSE.getOrDefault(className, new HashSet<>());
                orDefault.add(param);
                RESULT_PARSE.put(className,orDefault);
                strings.add(invokeInfo);
            }
        }


    }

    private boolean containString(String invokeInfo){
        for (String str: containString){
            String className = str.replaceAll(Constant.CLASS_SPLIT_POINT, Constant.CLASS_SPLIT_SLASH);
            if (invokeInfo.contains(className)){
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public Map<String,Set<String>> getResult(){
        return RESULT_PARSE;
    }


}
