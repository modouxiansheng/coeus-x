package com.modou.coeus.parse.cycle;

import com.sun.org.glassfish.gmbal.NameValue;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-02-27 18:30
 **/
public class CyCleSub1 extends CyCleMain{


    private String name;

    @Override
    public void invoke() {
        super.invoke();
        System.out.println("CyCleSub1");
    }
}
