package com.modou.coeus.parse.analysis;

import com.modou.coeus.parse.chart.A;

import javax.xml.bind.annotation.XmlValue;

/**
 * @program: coeus
 * @description:
 * @author: hu_pf
 * @create: 2021-08-14 00:52
 **/
public class TestInterfaceImpl extends TestExtend implements TestInterface {


    @XmlValue
    private String test2233;

    @Override
    public void invoke() {
        if ("xxxx".equals(test2233)){

        }
        if ("xxx".equals(ValusStatic.VALUE)){

        }

        if ("test".equals(A.TEST_VALUE)){

        }

    }
}
