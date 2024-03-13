package com.modou.coeus.parse.pass;

/**
 * @program: gadget-inspector
 * @description:
 * @author: hu_pf
 * @create: 2023-09-30 13:02
 **/
public class TestHpfGet {

    private String xx;

    private String name;


    public String getName(String name){

        return name;
    }

    public String getAge(String name,String age,String xxx){
        return name + age + xxx;
    }

    public String getXx(){
        return xx;
    }

    public void setName(Student name, String age, String address){
        this.name = address + age + name.address;
    }

    public Student getStu(String name,Student student){

        Student student1 = new Student();

        student1.setAge(name);

        student1.setAddress(student.getAddress());

        return student;
    }

    public static class Student{
        private String address;

        private String age;

        public void setAddress(String address) {
            this.address = address;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getAddress() {
            return address;
        }

        public String getAge() {
            return age;
        }
    }
}
