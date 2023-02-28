package com.modou.coeus.parse.chart;

import javax.xml.bind.annotation.XmlValue;

/**
 * @program: coeus
 * @description:
 * @author: hu_pf
 * @create: 2021-08-14 00:42
 **/
public class C {

    @XmlValue
    public String testC;

    public String testGetMethod;

    public void invoke(){

        if (testC.equals("xx3434")){

        }
        D d = new D();
        d.invoke();
    }

    @XmlValue

    public void setTestGetMethod(String s){
        this.testGetMethod = s;
    }
}
