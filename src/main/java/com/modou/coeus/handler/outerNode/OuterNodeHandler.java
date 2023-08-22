package com.modou.coeus.handler.outerNode;

/**
 * @program: Coeus
 * @description: 内部节点的处理类
 * @author: hu_pf
 * @create: 2021-08-13 21:30
 **/
public interface OuterNodeHandler<T,K> {

    K initialization(T t);

    Class<?> getClassType();
}
