package com.modou.coeus.handler.outerNode;

import com.modou.coeus.common.ClassRouter;
import com.modou.coeus.common.Constant;
import com.modou.coeus.common.NodeHandlerFactory;
import com.modou.coeus.node.CoeusMethodNode;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

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
            validParam(methodNode,next);
            coeusMethodNode.visit(nodeHandlerFactory.getInsnNodeHandler(next.getClass()),next);
        }
        coeusMethodNode.initAnnotationInfo(methodNode.visibleAnnotations, (AnnotationNodeHandler) nodeHandlerFactory.getOuterNodeHandler(AnnotationNode.class));

        return coeusMethodNode;
    }

    private void validParam(MethodNode methodNode,AbstractInsnNode instruction){
        if (methodNode.name.equals("buildName")) {
            boolean isAssign = false;
            if (instruction instanceof JumpInsnNode) {
                // 处理if语句
                JumpInsnNode jumpInsnNode = (JumpInsnNode) instruction;
                int targetIndex = methodNode.instructions.indexOf(jumpInsnNode.label);
                AbstractInsnNode target = methodNode.instructions.get(targetIndex + 1);
                if (target instanceof VarInsnNode && target.getOpcode() == Opcodes.ASTORE
                        && ((VarInsnNode) target).var == 0) {
                    // 如果是将参数存储到局部变量0中，则认为是对name进行赋值的指令
                    isAssign = true;
                }
            } else if (instruction instanceof VarInsnNode && instruction.getOpcode() == Opcodes.ASTORE
                    && ((VarInsnNode) instruction).var == 0) {
                // 如果是将参数存储到局部变量0中，则认为是对name进行赋值的指令
                isAssign = true;
            }
            if (isAssign) {
                System.out.println("name 被赋值了");
            } else {
//                System.out.println("name 没有被赋值");
            }
        }
    }

    @Override
    public Class getClassType() {
        return MethodNode.class;
    }
}
