package com.modou.coeus.utils;

import com.modou.coeus.node.CoeusClassNode;
import com.modou.coeus.node.CoeusParamNode;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @program: coeus-x
 * @description: 参数解析工具类
 * @author: hu_pf
 * @create: 2023-03-06 11:14
 **/
public class ParamParseUtils {

    /**
    * @Description: 根据入参的 classNode,和paramNode,看这个类中是否有对此参数赋值
    * @Param: [classNode(需要校验的类Code 信息), coeusParamNode(需要寻找的参数信息)]
    * @return: java.util.List<java.lang.String>: 返回所有的方法名称
    * @Author: hu_pf
    * @Date: 2023/3/6
    */
    public static List<String> getMethodsAssigningParameter(CoeusClassNode classNode, CoeusParamNode coeusParamNode) {
        ClassNode cn = classNode.getMetadata();
        List<String> methods = new ArrayList<>();
        for (MethodNode mn : cn.methods) {
            if (isParameterAssigned(mn, coeusParamNode)) {
                methods.add(mn.name);
            }
        }
        return methods;
    }

    /**
    * @Description: 判断方法中是否对这个参数赋值
    * @Param: [mn, coeusParamNode]
    * @return: boolean true: 有对这个参数赋值
    * @Author: hu_pf
    * @Date: 2023/3/6
    */
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

    /**
     * @Description: 判断方法中是否对这个参数赋值 只根据名称,不根据 desc
     * @Param: [mn, coeusParamNode]
     * @return: boolean true: 有对这个参数赋值
     * @Author: hu_pf
     * @Date: 2023/3/6
     */
    public static boolean isParameterAssigned(MethodNode mn, String paramName) {
        Iterator<AbstractInsnNode> it = mn.instructions.iterator();
        AbstractInsnNode currentNode = null;
        while (it.hasNext()) {
            AbstractInsnNode instruction = it.next();
            if (instruction.getOpcode() == Opcodes.ALOAD && ((VarInsnNode) instruction).var == 0) {
                currentNode = instruction.getNext();
            }
            if (instruction.getOpcode() == Opcodes.PUTFIELD && currentNode != null) {
                FieldInsnNode fieldInsnNode = (FieldInsnNode) instruction;
                if (fieldInsnNode.name.equals(paramName)) {
                    return true;
                }
                currentNode = null;
            }
        }
        return false;
    }
}
