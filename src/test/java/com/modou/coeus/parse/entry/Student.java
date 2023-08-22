package com.modou.coeus.parse.entry;

import java.util.List;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-08-03 10:46
 **/
public class Student {

    private String name;

    private Integer age;

    private List<Order> orderList;

    private Boolean flag;

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public static class Order{

        private String no;

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }
    }


}
