package com.modou.coeus.ability.data;

import java.util.Objects;

/**
 * @program: coeus-x
 * @description: 收集到的注解信息
 * @author: hu_pf
 * @create: 2023-02-28 14:10
 **/
public class AnnotationForValueData {

    private String name;

    private String value;

    public AnnotationForValueData(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnnotationForValueData that = (AnnotationForValueData) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
