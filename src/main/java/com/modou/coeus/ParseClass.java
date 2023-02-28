package com.modou.coeus;

import com.modou.coeus.common.ClassRouter;
import com.modou.coeus.handler.InitClassNodeOperate;
import com.modou.coeus.handler.innerNode.InsnNodeHandler;
import com.modou.coeus.handler.innerNode.MethodInsnNodeHandler;
import com.modou.coeus.node.CoeusClassNode;
import com.modou.coeus.node.CoeusMethodNode;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.tree.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * @program: com.zhhiyp.incubator.asm
 * @description: 解析class类
 * @author: hu_pf
 * @create: 2021-01-12 11:42
 **/
public class ParseClass {

    private static final ClassRouter classRouter = ClassRouter.getInstance();


    public static void parseSourceClass(InputStream in) {
        ClassNode cn = new ClassNode();
        try {
            ClassReader cr = new ClassReader(in);
            cr.accept(cn, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String className = cn.name;

        classRouter.putClass(className,cn);

        classRouter.getClass(className).visit(new InitClassNodeOperate());
    }
}
