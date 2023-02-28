package com.modou.coeus.common;

import com.modou.coeus.handler.innerNode.DefaultMethodInsnNodeHandler;
import com.modou.coeus.handler.innerNode.InsnNodeHandler;
import com.modou.coeus.handler.innerNode.MethodInsnNodeHandler;
import com.modou.coeus.handler.innerNode.ParamInsnNodeHandler;
import com.modou.coeus.handler.outerNode.AnnotationNodeHandler;
import com.modou.coeus.handler.outerNode.MethodNodeHandler;
import com.modou.coeus.handler.outerNode.OuterNodeHandler;
import com.modou.coeus.handler.outerNode.ParamNodeHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-02-27 17:16
 **/
public class NodeHandlerFactory {

    // 处理类的路由
    private static Map<Class, InsnNodeHandler> insnNodeHandlerMap = new HashMap<>();

    // 处理外部类的路由
    private static Map<Class, OuterNodeHandler> outerNodeHandlerMap = new HashMap<>();

    private static NodeHandlerFactory nodeHandlerFactory= new NodeHandlerFactory();


    {
        // todo 内部类型处理器要改成动态加载实现类而不是手动加
        MethodInsnNodeHandler methodInsnNodeHandler = new MethodInsnNodeHandler();
        insnNodeHandlerMap.put(methodInsnNodeHandler.getClassType(),methodInsnNodeHandler);

        ParamInsnNodeHandler paramInsnNodeHandler = new ParamInsnNodeHandler();
        insnNodeHandlerMap.put(paramInsnNodeHandler.getClassType(),paramInsnNodeHandler);


        MethodNodeHandler methodNodeHandler = new MethodNodeHandler();
        outerNodeHandlerMap.put(methodNodeHandler.getClassType(),methodNodeHandler);

        AnnotationNodeHandler annotationNodeHandler = new AnnotationNodeHandler();
        outerNodeHandlerMap.put(annotationNodeHandler.getClassType(),annotationNodeHandler);

        ParamNodeHandler paramNodeHandler = new ParamNodeHandler();
        outerNodeHandlerMap.put(paramNodeHandler.getClassType(),paramNodeHandler);

    }

    private NodeHandlerFactory(){

    }

    public static NodeHandlerFactory getInstance(){
        if (nodeHandlerFactory == null){
            nodeHandlerFactory = new NodeHandlerFactory();
        }
        return nodeHandlerFactory;
    }


    /**
     * @Description: 获得内部处理节点器
     * @Param: [classType]
     * @return: com.modou.coeus.handler.innerNode.InsnNodeHandler
     * @Author: hu_pf
     * @Date: 2021/8/13
     */
    public InsnNodeHandler getInsnNodeHandler(Class classType){
        InsnNodeHandler insnNodeHandler = insnNodeHandlerMap.get(classType);
        if (insnNodeHandler == null){
            insnNodeHandler = new DefaultMethodInsnNodeHandler();
        }
        return insnNodeHandler;
    }

    /**
     * @Description: 获取外部类的处理
     * @Param: [classType]
     * @return: com.modou.coeus.handler.outerNode.OuterNodeHandler
     * @Author: hu_pf
     * @Date: 2023/2/27
     */
    public OuterNodeHandler getOuterNodeHandler(Class classType){
        return outerNodeHandlerMap.get(classType);
    }
}
