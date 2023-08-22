package com.modou.coeus.ability.scan;

import com.modou.coeus.node.CoeusMethodNode;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: coeus-x
 * @description: 对于异常的扫描
 * @author: hu_pf
 * @create: 2023-08-09 11:35
 **/
public class ScanCallHandlerForException extends AbstractScanCallHandler{



    @Override
    public void doInvoke(ScanCallHandlerData scanCallHandlerData) {


        List<Object> codeExceptionArgs = getCodeExceptionArgs(scanCallHandlerData.getCoeusMethodNodeCurrent().getMetaData(),scanCallHandlerData.getCoeusMethodNodeCurrent());
        List<Object> codeExceptionArgs2 = getCodeExceptionArgs(scanCallHandlerData.getCoeusMethodNodePre().getMetaData(),scanCallHandlerData.getCoeusMethodNodeCurrent());
//
//        codeExceptionArgs.forEach(e-> System.out.println(e));
//        codeExceptionArgs2.forEach(e-> System.out.println(e));
    }


    public static List<Object> getCodeExceptionArgs(MethodNode methodNode, CoeusMethodNode coeusMethodNode) {
        List<Object> codeExceptionArgs = new ArrayList<>();

        InsnList instructions = methodNode.instructions;
        boolean isCodeException = false;
        for (AbstractInsnNode insnNode : instructions.toArray()) {
            if (insnNode.getOpcode() == Opcodes.ATHROW) {
                // 在抛出异常之前，获取 CodeException 的入参值
                codeExceptionArgs.add(getCodeExceptionArg(insnNode, methodNode,coeusMethodNode));
                isCodeException = false; // 抛出异常后，标记为不处于 CodeException 范围内
            }
        }

        return codeExceptionArgs;
    }

    private static Object getCodeExceptionArg(AbstractInsnNode insnNode, MethodNode methodNode,CoeusMethodNode coeusMethodNode) {
        int index = methodNode.instructions.indexOf(insnNode);
        for (int i = index - 1; i >= 0; i--) {
            AbstractInsnNode prevInsnNode = methodNode.instructions.get(i);
            if (prevInsnNode instanceof LdcInsnNode) {
                LdcInsnNode ldcInsnNode = (LdcInsnNode) prevInsnNode;
//                return ldcInsnNode.cst;
            }
            if (prevInsnNode instanceof  FieldInsnNode){
                FieldInsnNode fieldInsnNode = (FieldInsnNode) prevInsnNode;
//                return fieldInsnNode.owner +"."+ fieldInsnNode.name;
            }
            if (prevInsnNode instanceof MethodInsnNode){
                MethodInsnNode methodInsnNode = (MethodInsnNode) prevInsnNode;
//                if (methodInsnNode.name.contains("get")){
//                    return methodInsnNode.owner + "." + methodInsnNode.name;
//                }
                if (methodInsnNode.owner.contains("Exception")){
                    System.out.println(methodInsnNode.owner);
                }
            }
        }
        return null;
    }



}
