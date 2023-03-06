package com.modou.coeus.parse.scan.chain;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-03-06 11:46
 **/
public class ScanCallHandlerForParamCallChainTestC {

    private String name;

    public void invoke(){
        setName("xxxx");
    }

    public void setName(String name) {
        this.name = name;
    }

    public void buildName(){
        this.name = "buildName";
    }
}
