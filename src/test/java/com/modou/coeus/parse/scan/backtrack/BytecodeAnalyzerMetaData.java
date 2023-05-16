package com.modou.coeus.parse.scan.backtrack;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-04-24 09:51
 **/
public class BytecodeAnalyzerMetaData {

    private String name;

    private Integer age;

    private String adress;


    public void setName(){
        String name = "xxxxx";
        this.name = name;
    }

    public void setAge(Integer age ){
        this.age = age;
    }

    public void setAddress(){
        this.adress = getAddress();
    }



    public String getAddress(){
        return "xxx";
    }
}
