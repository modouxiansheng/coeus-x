package com.modou.coeus.parse.scan.chain;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-03-06 11:46
 **/
public class ScanCallHandlerForParamCallChainTestB {

    public void invoke(){
        ScanCallHandlerForParamCallChainTestC scanCallHandlerForParamCallChainTestC = new ScanCallHandlerForParamCallChainTestC();
        scanCallHandlerForParamCallChainTestC.invoke();
    }
}
