package com.modou.coeus.parse.parse;

import com.modou.coeus.NodeFacade;
import com.modou.coeus.ability.data.AnnotationForValueData;
import com.modou.coeus.common.ClassRouter;
import com.modou.coeus.node.CoeusClassNode;
import com.modou.coeus.node.CoeusMethodNode;

import java.util.Set;
import java.util.function.Function;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-02-28 15:50
 **/
public class ParseTest {


    public static void main(String[] args) {
//        String projectRoot = "/Users/admin/git/trade_order_centric";
//        NodeFacade.buildSource(projectRoot);
//
//        ClassRouter instance = ClassRouter.getInstance();
//        instance.initSubClass();
//
//        CoeusClassNode aClass = instance.getClass("com.shizhuang.duapp.trade.order.interfaces.facade.service.core.general.unit.OrderBuyerUnitApiImpl");
//        CoeusMethodNode invoke = aClass.getMethod("createOrder");
//        Set<AnnotationForValueData> annotationForValueData = NodeFacade.parseInvoke(invoke, instance);
//        annotationForValueData.stream().forEach(e->{
//            System.out.print(e.getName() + "MMMMMMMMMM"+e.getValue());
//            System.out.println("换行换行");
//        });

        System.out.println(test(ParseTest::getString,"hupengfei"));
    }

    private static String getString(String name){
        return name;
    }

    private static String testxxx(){
        return "xxxx";
    }

    private static <T,R> R test(Function<T, R> function,T t){
        return function.apply(t);
    }
}
