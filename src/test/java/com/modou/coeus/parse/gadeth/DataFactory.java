package com.modou.coeus.parse.gadeth;

public interface DataFactory<T> {
    T parse(String[] fields);
    String[] serialize(T obj);
}
