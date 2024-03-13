package com.modou.coeus.parse.pass;

import com.modou.coeus.ability.scan.ScanCallChainAbility;
import com.modou.coeus.ability.scan.ScanCallHandlerForParamBacktrack;
import com.modou.coeus.node.CoeusMethodNode;
import com.modou.coeus.utils.GraphCall;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-10-12 12:00
 **/
public class ScanCallHandlerForParamBacktrackTest {




    public static void main(String[] args) {
        String projectRoot = "/Users/admin/mygit/coeus-x/target/test-classes/com/modou/coeus/parse/pass/order";
//        String projectRoot = "/Users/admin/git/trade_order_centric";


        ScanCallHandlerForParamBacktrack scanCallHandlerForParamCallChain = new ScanCallHandlerForParamBacktrack();
        ScanCallChainAbility scanCallChainAbility4 = new ScanCallChainAbility(projectRoot,scanCallHandlerForParamCallChain);
        scanCallChainAbility4.invoke("com.modou.coeus.parse.pass.order.OrderBuyUnitTest","invoke");
//        scanCallChainAbility4.invoke("com.shizhuang.duapp.trade.order.interfaces.facade.service.core.general.unit.OrderBuyerUnitApiImpl","createOrder");
        List<List<CoeusMethodNode>> callHandlerForParamCallChain3 = scanCallHandlerForParamCallChain.getTraces2("com.modou.coeus.parse.pass.order.OrderDO#price");

        Set<GraphCall> discoveredCallsTeet = ScanCallHandlerForParamBacktrack.discoveredCalls;

        System.out.println("1");
    }
}
