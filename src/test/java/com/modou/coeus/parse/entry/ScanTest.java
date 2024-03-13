package com.modou.coeus.parse.entry;

import com.modou.coeus.ability.scan.ScanCallChainAbility;
import com.modou.coeus.ability.scan.ScanCallHandlerForEntryQuotation;

import java.util.*;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-08-03 11:17
 **/
public class ScanTest {

    private static Map<String,String> RESULT = new HashMap<>();

    static {
        RESULT.put("/Users/admin/git/discounts-center","com.shizhuang.duapp.discount.interfaces.mq.consumer.OrderCreateConsumer#handleMsg");
        RESULT.put("/Users/admin/git/duapp-oversea","com.shizhuang.duapp.oversea.mq.customer.TradeOrderCreatedConsumer#handle");
        RESULT.put("/Users/admin/git/duapp-oversea","com.shizhuang.duapp.oversea.mq.customer.ordertrack.OrderTrackCreateConsumer#handle");
        RESULT.put("/Users/admin/git/merchant-distribution","com.shizhuang.duapp.merchant.distribution.interfaces.mq.consumer.TradeOrderCreateMqConsumer#handle");
        RESULT.put("/Users/admin/git/merchant-side-base","com.shizhuang.duapp.merchant.side.facade.mq.push.TradeOrderCreateConsumer#handle");
        RESULT.put("/Users/admin/git/trade-deposit-center","com.shizhuang.duapp.deposit.application.mq.consumer.order.TradeOrderCreateProcessor#doHandle");
        RESULT.put("/Users/admin/git/trade-bidding-center","com.shizhuang.duapp.bidding.application.mq.consumer.order.TradeOrderCreatedConsumer#handle");
        RESULT.put("/Users/admin/git/trade-store-center","com.shizhuang.duapp.trade.store.application.mq.consumer.order.TradeOrderCreateProcessor#handle");
        RESULT.put("/Users/admin/git/order-fulfillment-strategy","com.poizon.scm.ofs.interfaces.mq.recketmq.consumer.TradeOrderCreatedConsumer#handle");
        RESULT.put("/Users/admin/git/order-fulfillment-center","com.poizon.scm.bizcore.preallocated.mq.consumer.TradeOrderCreatedConsumer#handle");
        RESULT.put("/Users/admin/git/duapp-tms","com.shizhuang.duapp.tms.service.infrastructure.mq.DeliveryInfoMqConsumerNew#handle");
        RESULT.put("/Users/admin/git/cross-border-center","com.poizon.overseas.cbc.interfaces.dal.mq.order.OrderInventoryOccUpOnsNewTopicConsumer#handle");

    }

    // tms: com.shizhuang.duapp.tms.service.application.dto.order.TradeOrderInfo
    // com.shizhuang.duapp.tms.service.application.dto.order.TradeAddressInfo
    // com.shizhuang.duapp.tms.service.application.dto.order.TradeSubOrderInfo
    // com.shizhuang.duapp.tms.service.application.dto.order.TradeFreightInfo
    // com.shizhuang.duapp.tms.service.application.dto.order.DeliveryInfo
    //

    public static void main(String[] args) {
        String projectRoot = "/Users/admin/git/duapp-tms";
        List<String> containString = new ArrayList<>();
        containString.add("com.shizhuang.duapp.tms.service.application.dto.order.TradeOrderInfo");
        containString.add("com.shizhuang.duapp.tms.service.application.dto.order.TradeAddressInfo");
        containString.add("com.shizhuang.duapp.tms.service.application.dto.order.TradeSubOrderInfo");
        containString.add("com.shizhuang.duapp.tms.service.application.dto.order.TradeFreightInfo");
        containString.add("com.shizhuang.duapp.tms.service.application.dto.order.DeliveryInfo");
        containString.add("com.shizhuang.duapp.tms.service.application.dto.oversea.OverSeasTradeOrderInfo");
        containString.add("com.shizhuang.duapp.tms.service.application.dto.oversea.OverSeasTradeAddressInfo");
        containString.add("com.shizhuang.duapp.tms.service.application.dto.oversea.OverSeasTradeSubOrderInfo");
        containString.add("com.shizhuang.duapp.tms.service.application.dto.oversea.OverSeasDeliveryInfo");
        containString.add("com.shizhuang.duapp.tms.service.application.dto.oversea.OverSeasTradeFreightInfo");
        containString.add("com.shizhuang.duapp.tms.service.application.dto.oversea.OverseaContent");
        ScanCallHandlerForEntryQuotation scanCallHandlerForValueAnnotation = new ScanCallHandlerForEntryQuotation(containString);
        ScanCallChainAbility scanCallChainAbility = new ScanCallChainAbility(projectRoot,scanCallHandlerForValueAnnotation);
        scanCallChainAbility.invoke("com.shizhuang.duapp.tms.service.infrastructure.mq.DeliveryInfoMqConsumerNew","handle");
        Map<String, Set<String>> result = scanCallHandlerForValueAnnotation.getResult();
        for (String key: result.keySet()){
            Set<String> params = result.get(key);
            for (String param :params){
                System.out.println(key+":"+param);
            }
        }
    }
}
