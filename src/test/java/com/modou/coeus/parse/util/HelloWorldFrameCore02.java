package com.modou.coeus.parse.util;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;


public class HelloWorldFrameCore02 {
    public static void main(String[] args) {
        String relative_path = "com/modou/coeus/parse/pass/HelloTest.class";
        String filepath = FileUtils.getFilePath(relative_path);
        byte[] bytes = FileUtils.readBytes(filepath);

        //（1）构建ClassReader
        ClassReader cr = new ClassReader(bytes);

        //（2）构建ClassVisitor
        int api = Opcodes.ASM5;
        ClassVisitor cv = new MethodStackMapFrame02Visitor(api, null, bytes);

        //（3）结合ClassReader和ClassVisitor
        int parsingOptions = ClassReader.EXPAND_FRAMES; // 注意，这里使用了EXPAND_FRAMES
        cr.accept(cv, parsingOptions);
    }
}
