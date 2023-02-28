package com.modou.coeus.parse.chart;

import com.modou.coeus.parse.chart.ext.AConfig;
import com.modou.coeus.parse.chart.ext.Config;

import javax.xml.bind.annotation.XmlValue;

/**
 * @program: coeus
 * @description:
 * @author: hu_pf
 * @create: 2021-08-14 00:42
 **/
public class A {

    public static String TEST_VALUE = "xxxxxxxxxx";

    @XmlValue
    private String test2233;

    private Config config = new AConfig();

    public void invoke(){
        if (test2233.equals("xx3434")){

        }
        config.getName();
        B b = new B();
        b.invoke();
    }

}
