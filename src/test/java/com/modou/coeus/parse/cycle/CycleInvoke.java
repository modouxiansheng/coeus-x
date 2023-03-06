package com.modou.coeus.parse.cycle;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-02-27 18:32
 **/
public class CycleInvoke {

    private static String test;

    public static void main(String[] args) {
        invoke();
    }

    public static void invoke(){
        String name = test;
        CyCleMain cyCleMain = new CyCleMain();
        cyCleMain.invoke();
    }
}
