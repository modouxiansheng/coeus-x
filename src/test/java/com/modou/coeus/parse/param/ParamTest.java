package com.modou.coeus.parse.param;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-03-05 15:02
 **/
public class ParamTest {

    private String name;

    private String address;

    public void buildName(String name){
        if (name == "xxxx"){
            this.name = "sdsds";
            this.address = "xxx";
        }else if (name == "sdsd"){
            this.name = "sdsdsdasd";
        }
        this.name = name;
    }

    public void setAddress(String address){
        this.address = address;
        this.name = "xxxx";
    }
}
