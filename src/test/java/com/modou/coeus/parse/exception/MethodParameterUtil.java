package com.modou.coeus.parse.exception;


import jdk.internal.org.objectweb.asm.*;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

import java.lang.reflect.Method;

public class MethodParameterUtil {

    public static String[] getMethodParameterNames(Method method) throws Exception {
        Class<?> clazz = method.getDeclaringClass();
        int parameterCount = method.getParameterCount();
        ClassReader classReader = new ClassReader(clazz.getName());
        String[] parameterNames = new String[parameterCount];

        classReader.accept(new ClassVisitor(Opcodes.ASM5) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                if (!name.equals(method.getName())) {
                    return super.visitMethod(access, name, desc, signature, exceptions);
                }
                return new MethodNode(Opcodes.ASM5, access, name, desc, signature, exceptions) {
                    @Override
                    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                        if (index >= 0 && index < parameterCount) {
                            parameterNames[index] = name;
                        }
                        super.visitLocalVariable(name, desc, signature, start, end, index);
                    }
                };
            }
        }, 0);

        return parameterNames;
    }

    public static void main(String[] args) throws Exception {
        Method method = MyClass.class.getMethod("myMethod", int.class, String.class);
        String[] parameterNames = getMethodParameterNames(method);

        for (int i = 0; i < parameterNames.length; i++) {
            System.out.println("Parameter " + (i + 1) + ": " + parameterNames[i]);
        }
    }

    static class MyClass {
        public void myMethod(int param1, String param2) {
            // do something
        }
    }
}