package com.modou.coeus.handler.outerNode;

import com.modou.coeus.common.ClassRouter;
import com.modou.coeus.common.Constant;
import com.modou.coeus.common.NodeHandlerFactory;
import com.modou.coeus.node.CoeusMethodNode;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.AnnotationNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

import java.util.ListIterator;

/**
 * @program: coeus-x
 * @description: 方法处理器
 * @author: hu_pf
 * @create: 2023-02-27 16:11
 **/
public class MethodNodeHandler implements OuterNodeHandler<MethodNode,CoeusMethodNode>{


    private static final NodeHandlerFactory nodeHandlerFactory = NodeHandlerFactory.getInstance();

    /**
    * @Description: 初始化方法整体属性
    * @Param: [methodNode]
    * @return: com.modou.coeus.node.CoeusMethodNode
    * @Author: hu_pf
    * @Date: 2023/2/27
    */
    @Override
    public CoeusMethodNode initialization(MethodNode methodNode) {
        if (Constant.INIT_METHOD_NAME.equals(methodNode.name)){
            return null;
        }
        CoeusMethodNode coeusMethodNode = new CoeusMethodNode(methodNode.name,methodNode.desc);

        ListIterator<AbstractInsnNode> iterator = methodNode.instructions.iterator();

        while (iterator.hasNext()) {
            AbstractInsnNode next = iterator.next();
            coeusMethodNode.visit(nodeHandlerFactory.getInsnNodeHandler(next.getClass()),next);
        }
        coeusMethodNode.initAnnotationInfo(methodNode.visibleAnnotations, (AnnotationNodeHandler) nodeHandlerFactory.getOuterNodeHandler(AnnotationNode.class));

        return coeusMethodNode;
    }

    @Override
    public Class getClassType() {
        return MethodNode.class;
    }
}
