package com.modou.coeus.parse.pass;

import java.io.IOException;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-10-12 10:32
 **/
public class PassTest {

    public String getName(String name,String age){

        Student student = new Student();

        String name1 = getString();
        student.setName(name1,name);

//        Student2 student2 = new Student2();
//
//        student2.setName2(name,age);

        return name;
    }

    private String getString(){
        return "xxx";
    }



    public static class Student{
        private String name;

        public void setName(String name,String age){
            this.name = name;
        }
    }

    public static class Student2{
        private String name;

        public void setName2(String name,String age){
            this.name = name;
        }
    }

}
