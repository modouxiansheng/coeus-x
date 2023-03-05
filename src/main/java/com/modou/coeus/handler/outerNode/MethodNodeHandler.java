package com.modou.coeus.handler.outerNode;

import com.modou.coeus.common.ClassRouter;
import com.modou.coeus.common.Constant;
import com.modou.coeus.common.NodeHandlerFactory;
import com.modou.coeus.node.CoeusMethodNode;
import com.modou.coeus.node.CoeusParamNode;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

    public static List<String> getMethodsAssigningParameter(ClassNode cn, CoeusParamNode coeusParamNode) {
        List<String> methods = new ArrayList<>();
        for (MethodNode mn : cn.methods) {
            if (isParameterAssigned(mn, coeusParamNode)) {
                methods.add(mn.name);
            }
        }
        return methods;
    }
    public static boolean isParameterAssigned(MethodNode mn, CoeusParamNode coeusParamNode) {
        Iterator<AbstractInsnNode> it = mn.instructions.iterator();
        AbstractInsnNode currentNode = null;

        while (it.hasNext()) {
            AbstractInsnNode instruction = it.next();
            if (instruction.getOpcode() == Opcodes.ALOAD && ((VarInsnNode) instruction).var == 0) {
                currentNode = instruction.getNext();
            }
            if (instruction.getOpcode() == Opcodes.PUTFIELD && currentNode != null) {
                FieldInsnNode fieldInsnNode = (FieldInsnNode) instruction;
                if (fieldInsnNode.name.equals(coeusParamNode.name) && fieldInsnNode.desc.equals(coeusParamNode.desc)) {
                    return true;
                }
                currentNode = null;
            }
        }
        return false;
    }

    @Override
    public Class getClassType() {
        return MethodNode.class;
    }
}
