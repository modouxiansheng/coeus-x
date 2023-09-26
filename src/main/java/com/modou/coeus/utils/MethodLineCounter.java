package com.modou.coeus.utils;

import com.modou.coeus.node.Line;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-09-24 14:54
 **/
public class MethodLineCounter {

    public static Line countLines(MethodNode methodNode){
        final int[] startLine = {0};
        final int[] endLine = {0};

        MethodVisitor methodVisitor = new MethodVisitor(Opcodes.ASM5) {
            @Override
            public void visitLineNumber(int line, Label start) {
                if (startLine[0] == 0) {
                    startLine[0] = line;
                }
                endLine[0] = line;
            }
        };
        methodNode.accept(methodVisitor);
        return new Line(startLine[0],endLine[0]);
    }
}
