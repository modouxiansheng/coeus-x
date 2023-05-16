package com.modou.coeus.parse.scan.backtrack;

import com.modou.coeus.NodeFacade;
import com.modou.coeus.ability.scan.ScanCallChainAbility;
import com.modou.coeus.ability.scan.ScanCallHandlerForParamCallChain;
import com.modou.coeus.common.ClassRouter;
import com.modou.coeus.node.CoeusClassNode;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.analysis.AnalyzerException;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-04-24 09:50
 **/
public class BytecodeAnalyzerMain {

    public static void main(String[] args) throws AnalyzerException {
        String projectRoot = "/Users/admin/mygit/coeus-x/target/test-classes/com/modou/coeus/parse/scan/backtrack";
        NodeFacade.buildSource(projectRoot);

        ClassRouter instance = ClassRouter.getInstance();
        instance.initSubClass();

        CoeusClassNode aClass = instance.getClass("com.modou.coeus.parse.scan.backtrack.BytecodeAnalyzerMetaData");
        ClassNode metadata = aClass.getMetadata();
        BytecodeAnalyzer.invoke(metadata);
    }
}
