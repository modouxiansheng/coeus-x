package com.modou.coeus.parse.cycle;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-02-27 18:30
 **/
public class CyCleMain {

    @TestValue("xxxx")
    private String testValue;

    public void invoke(){
        if ("text".equals(testValue)){
            System.out.println("CyCleMain");
        }
    }
}
