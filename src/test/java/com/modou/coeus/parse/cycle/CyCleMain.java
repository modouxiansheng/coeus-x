package com.modou.coeus.parse.cycle;

import java.util.function.Function;

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
            System.out.println(test("",CyCleMain::getString));
        }
    }


    static String getString(String s){
        return "name";
    }

    public String test(String name, Function<String,String> invoke){
        return invoke.apply("xxx");
    }
}
