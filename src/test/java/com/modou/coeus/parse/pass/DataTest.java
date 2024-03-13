package com.modou.coeus.parse.pass;

import com.modou.coeus.parse.chart.D;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-10-09 18:15
 **/
public class DataTest {

    private String name;


    public String getName(String name,Integer age,String test){

        String result = name + test + "xxx";
        System.out.println(result);
        return name;
    }

    public Demo getDemo(String name,Integer age,String test){
        Demo demo = new Demo();
        demo.setAge(age);
        demo.setName(name);
        return demo;
    }

    public void setName(Demo name,String age,String address){
        this.name = address + age;
    }



    static class Demo{
        private String name;
        private Integer age;

        public void setName(String name) {
            this.name = name;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public Integer getAge() {
            return age;
        }
    }

}
