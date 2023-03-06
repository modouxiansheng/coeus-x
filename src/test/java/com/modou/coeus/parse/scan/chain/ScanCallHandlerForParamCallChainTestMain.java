package com.modou.coeus.parse.scan.chain;

import com.modou.coeus.ability.scan.ScanCallChainAbility;
import com.modou.coeus.ability.scan.ScanCallHandlerForParamCallChain;
import com.modou.coeus.common.ClassRouter;
import com.modou.coeus.node.CoeusClassNode;
import com.modou.coeus.node.CoeusMethodNode;

import java.util.List;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-03-06 11:49
 **/
public class ScanCallHandlerForParamCallChainTestMain {

    public static void main(String[] args) {
        String projectRoot = "/Users/admin/mygit/coeus-x/target/test-classes/com/modou/coeus/parse/scan";
        ScanCallHandlerForParamCallChain scanCallHandlerForParamCallChain = new ScanCallHandlerForParamCallChain();
        ScanCallChainAbility scanCallChainAbility = new ScanCallChainAbility(projectRoot,scanCallHandlerForParamCallChain);
        scanCallChainAbility.invoke("com.modou.coeus.parse.scan.chain.ScanCallHandlerForParamCallChainTest","invoke");

        List<List<CoeusMethodNode>> traces = scanCallHandlerForParamCallChain.getTraces("com.modou.coeus.parse.scan.chain.ScanCallHandlerForParamCallChainTestC#name");

        System.out.println("1");
    }
}
