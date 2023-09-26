package com.modou.coeus.parse.exception;

import com.modou.coeus.ability.scan.*;
import com.modou.coeus.node.CoeusMethodNode;

import java.util.List;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-08-09 11:38
 **/
public class ScanCallHandlerForExceptionTest {

    public static void main(String[] args) {
//        String projectRoot = "/Users/admin/git/trade_order_centric";
//        ScanCallHandlerForException scanCallHandlerForException = new ScanCallHandlerForException();
//        ScanCallChainAbility scanCallChainAbility = new ScanCallChainAbility(projectRoot,scanCallHandlerForException);
//        scanCallChainAbility.invoke("com.shizhuang.duapp.trade.order.interfaces.facade.service.core.general.unit.OrderBuyerUnitApiImpl","createOrder");


        String projectRoot = "/Users/admin/mygit/coeus-x/target/test-classes/com/modou/coeus/parse/exception";
        ScanCallHandlerForException scanCallHandlerForException = new ScanCallHandlerForException();
        ScanCallChainAbility scanCallChainAbility = new ScanCallChainAbility(projectRoot,scanCallHandlerForException);
        scanCallChainAbility.invoke("com.modou.coeus.parse.exception.A","run");

//        ScanCallHandlerFoMethodCallChain callHandlerFoMethodCallChain = new ScanCallHandlerFoMethodCallChain();
//        ScanCallChainAbility scanCallChainAbility2 = new ScanCallChainAbility(projectRoot,callHandlerFoMethodCallChain);
//        scanCallChainAbility2.invoke("com.modou.coeus.parse.exception.A","run");
//        List<List<CoeusMethodNode>> callHandlerForParamCallChain1 = callHandlerFoMethodCallChain.getTraces("com.modou.coeus.parse.exception.A.ErrorInfo#setMsg");
//        System.out.println("1");

        ScanCallHandlerForParamCallChain callHandlerForParamCallChain = new ScanCallHandlerForParamCallChain();
        ScanCallChainAbility scanCallChainAbility3 = new ScanCallChainAbility(projectRoot,callHandlerForParamCallChain);
        scanCallChainAbility3.invoke("com.modou.coeus.parse.exception.A","run");
        List<List<CoeusMethodNode>> callHandlerForParamCallChain2 = callHandlerForParamCallChain.getTraces("com.modou.coeus.parse.exception.CodeException#msg");
        System.out.println("1");


        ScanCallHandlerForParamBacktrack scanCallHandlerForParamCallChain = new ScanCallHandlerForParamBacktrack();
        ScanCallChainAbility scanCallChainAbility4 = new ScanCallChainAbility(projectRoot,scanCallHandlerForParamCallChain);
        scanCallChainAbility4.invoke("com.modou.coeus.parse.exception.A","run");
        List<List<CoeusMethodNode>> callHandlerForParamCallChain3 = scanCallHandlerForParamCallChain.getTraces("com.modou.coeus.parse.exception.CodeException#msg");
        System.out.println("1");
    }
}
