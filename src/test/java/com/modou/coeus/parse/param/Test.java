package com.modou.coeus.parse.param;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Test {
    public static void main(String[] args) throws IOException {
        InputStream inputStream = Test.class.getResourceAsStream("ParamTest.class");
        ClassReader cr = new ClassReader(inputStream);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);

        List<String> name = getMethodsAssigningParameter(cn, "address");

        name.forEach(System.out::println);
    }


    public static List<String> getMethodsAssigningParameter(ClassNode cn, String parameterName) {
        List<String> methods = new ArrayList<>();
        for (MethodNode mn : cn.methods) {
            if (isParameterAssigned(mn, parameterName)) {
                methods.add(mn.name);
            }
        }
        return methods;
    }
    public static boolean isParameterAssigned(MethodNode mn, String parameterName) {
        Iterator<AbstractInsnNode> it = mn.instructions.iterator();
        AbstractInsnNode currentNode = null;

        while (it.hasNext()) {
            AbstractInsnNode instruction = it.next();
            if (instruction.getOpcode() == Opcodes.ALOAD && ((VarInsnNode) instruction).var == 0) {
                currentNode = instruction.getNext();
            }
            if (instruction.getOpcode() == Opcodes.PUTFIELD && currentNode != null) {
                FieldInsnNode fieldInsnNode = (FieldInsnNode) instruction;
                if (fieldInsnNode.name.equals(parameterName) && fieldInsnNode.desc.equals("Ljava/lang/String;")) {
                    return true;
                }
                currentNode = null;
            }
        }
        return false;
    }


}
