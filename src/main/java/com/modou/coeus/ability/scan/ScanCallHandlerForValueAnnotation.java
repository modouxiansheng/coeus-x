package com.modou.coeus.ability.scan;

import com.modou.coeus.ability.data.AnnotationForValueData;
import com.modou.coeus.common.ClassRouter;
import com.modou.coeus.node.CoeusClassNode;
import com.modou.coeus.node.CoeusMethodNode;
import com.modou.coeus.node.CoeusParamNode;

import java.util.HashSet;
import java.util.Set;

/**
 * @program: coeus-x
 * @description: 对于@Value 注解的处理
 * @author: hu_pf
 * @create: 2023-03-05 17:59
 * doc: https://www.processon.com/diagraming/5fe9dddfe0b34d2934f07545
 **/
public class ScanCallHandlerForValueAnnotation implements ScanCallHandlerInterface{

    private final Set<AnnotationForValueData> valueDataStatic = new HashSet<>();

    private static final String VALUE_ANNOTATION = "Value";

    private static final String CONFIGURATION_ANNOTATION = "ConfigurationProperties";

    @Override
    public void invoke(CoeusMethodNode invoke, ClassRouter classRouter) {
        if (invoke == null || invoke.coeusParamNodes == null){
            return;
        }
        Set<AnnotationForValueData> valueData = new HashSet<>();
        // invoke.coeusParamNodes 是方法中所有的参数名称
        for (CoeusParamNode coeusParamNode : invoke.coeusParamNodes) {
            // 1. 找到这个参数所属的类
            CoeusClassNode aClass1 = classRouter.getClass(coeusParamNode.owner);
            // 2. 判断这个类中是否有这个属性
            if (aClass1 != null && aClass1.hasParamTer(coeusParamNode.name)){
                // 参数上有@Value
                CoeusParamNode coeusParamNode1 = aClass1.getCoeusParamNode(coeusParamNode.name);
                if (coeusParamNode1.containAnnotationName(VALUE_ANNOTATION)){
                    valueData.add(new AnnotationForValueData(coeusParamNode1.name,coeusParamNode1.getCoeusAnnotationNodeByName(VALUE_ANNOTATION).getStringValue("value")));
                }

                // 方法上有@Value
                if (aClass1.methods != null){
                    for (CoeusMethodNode method : aClass1.methods) {
                        if (method.getName().startsWith("set")){
                            if (method.coeusParamNodes != null){
                                for (CoeusParamNode paramNode : method.coeusParamNodes) {
                                    if (paramNode.name.equals(coeusParamNode.name) && method.containAnnotationName(VALUE_ANNOTATION)){
                                        valueData.add(new AnnotationForValueData(coeusParamNode1.name,method.getCoeusAnnotationNodeByName(VALUE_ANNOTATION).getStringValue("value")));
                                    }
                                }
                            }
                        }
                    }
                }

                // 类上面有@ConfigurationProperties
                if (aClass1.containAnnotationName(CONFIGURATION_ANNOTATION)){
                    valueData.add(new AnnotationForValueData(coeusParamNode1.name,aClass1.getCoeusAnnotationNodeByName(CONFIGURATION_ANNOTATION).getStringValue("prefix")));
                }

                // 是GrayArkUtils 这个类的

            }
        }
        valueDataStatic.addAll(valueData);
    }

    public Set<AnnotationForValueData> getValueData() {
        return valueDataStatic;
    }
}
