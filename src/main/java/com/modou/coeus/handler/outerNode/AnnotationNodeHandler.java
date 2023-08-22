package com.modou.coeus.handler.outerNode;

import com.modou.coeus.node.CoeusAnnotationNode;
import jdk.internal.org.objectweb.asm.tree.AnnotationNode;

/**
 * @program: coeus-x
 * @description: 注解处理器
 * @author: hu_pf
 * @create: 2023-02-27 16:11
 **/
public class AnnotationNodeHandler implements OuterNodeHandler<AnnotationNode,CoeusAnnotationNode>{

    @Override
    public CoeusAnnotationNode initialization(AnnotationNode annotationNode) {
        CoeusAnnotationNode coeusAnnotationNode = new CoeusAnnotationNode(annotationNode.desc);
        if (annotationNode.values != null){
            int i = 0;
            while (i < annotationNode.values.size()){
                coeusAnnotationNode.putKeyAndValue((String) annotationNode.values.get(i),annotationNode.values.get(i+1));
                i = i + 2;
            }
        }
        return coeusAnnotationNode;
    }

    @Override
    public Class<?> getClassType() {
        return AnnotationNode.class;
    }
}
