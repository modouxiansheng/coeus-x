package com.modou.coeus.handler.outerNode;

import com.modou.coeus.common.Constant;
import com.modou.coeus.common.NodeHandlerFactory;
import com.modou.coeus.node.CoeusMethodNode;
import com.modou.coeus.node.CoeusParamNode;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.AnnotationNode;
import jdk.internal.org.objectweb.asm.tree.FieldNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

import java.util.ListIterator;

/**
 * @program: coeus-x
 * @description: 参数处理器
 * @author: hu_pf
 * @create: 2023-02-27 16:11
 **/
public class ParamNodeHandler implements OuterNodeHandler<FieldNode, CoeusParamNode>{

    private static final NodeHandlerFactory nodeHandlerFactory = NodeHandlerFactory.getInstance();

    @Override
    public CoeusParamNode initialization(FieldNode fieldNode) {
        CoeusParamNode coeusParamNode = new CoeusParamNode(fieldNode.name, fieldNode.desc);
        coeusParamNode.initAnnotationInfo(fieldNode.visibleAnnotations, (AnnotationNodeHandler) nodeHandlerFactory.getOuterNodeHandler(AnnotationNode.class));
        coeusParamNode.access = fieldNode.access;
        return coeusParamNode;
    }

    @Override
    public Class<?> getClassType() {
        return FieldNode.class;
    }
}
