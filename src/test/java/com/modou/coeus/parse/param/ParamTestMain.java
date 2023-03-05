package com.modou.coeus.parse.param;

import com.modou.coeus.NodeFacade;
import com.modou.coeus.common.ClassRouter;
import com.modou.coeus.node.CoeusClassNode;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-03-05 15:03
 **/
public class ParamTestMain {

    public static void main(String[] args) throws Exception {
        String projectRoot = "/Users/admin/mygit/coeus-x/target/test-classes/com/modou/coeus/parse/param";
        NodeFacade.buildSource(projectRoot);

        ClassRouter instance = ClassRouter.getInstance();
        instance.initSubClass();

        CoeusClassNode aClass = instance.getClass("com.modou.coeus.parse.param.ParamTest");
    }
}
