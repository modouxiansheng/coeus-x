package com.modou.coeus.parse.pass.order;


/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2024-02-10 12:53
 **/

public class InvokeRequest {

    private String name;

    private Long price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}
