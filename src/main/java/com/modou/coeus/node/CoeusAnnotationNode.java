package com.modou.coeus.node;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: coeus
 * @description: 注解节点信息
 * @author: hu_pf
 * @create: 2021-03-08 10:03
 **/
public class CoeusAnnotationNode {

    private String name;

    private Map<String,Object> keyAndValue;


    public CoeusAnnotationNode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void putKeyAndValue(String key,Object value){
        if (keyAndValue == null){
            keyAndValue = new HashMap<>();
        }
        keyAndValue.put(key,value);
    }

    public String getStringValue(String key){
        if (keyAndValue == null){
            return null;
        }
        return (String) keyAndValue.get(key);
    }
}
