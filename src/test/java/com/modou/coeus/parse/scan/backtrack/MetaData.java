package com.modou.coeus.parse.scan.backtrack;

/**
 * @program: coeus-x
 * @description: 数据源
 * @author: hu_pf
 * @create: 2023-03-06 20:00
 **/
public class MetaData {

    public void getName(String name){
        MetaData1 metaData1 = new MetaData1();
        metaData1.name = name;
        invoke(metaData1);
    }


    public void invoke(MetaData1 metaData1){
        String namexxxxx = getData(metaData1);
        MetaData2 metaData2 = new MetaData2();
        metaData2.buildName(namexxxxx,metaData1.name);
    }

    private String getData(MetaData1 metaData1){
        return metaData1.name;
    }


    public static class MetaData1{
        public String name;
    }

    public static class MetaData2{
        private String name;

        public void buildName(String name,String add){
            this.name = name;
        }
    }
}
