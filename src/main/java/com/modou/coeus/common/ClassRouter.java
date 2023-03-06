package com.modou.coeus.common;

import com.modou.coeus.handler.innerNode.DefaultMethodInsnNodeHandler;
import com.modou.coeus.handler.innerNode.InsnNodeHandler;
import com.modou.coeus.handler.innerNode.MethodInsnNodeHandler;
import com.modou.coeus.handler.innerNode.ParamInsnNodeHandler;
import com.modou.coeus.handler.outerNode.MethodNodeHandler;
import com.modou.coeus.handler.outerNode.OuterNodeHandler;
import com.modou.coeus.node.CoeusClassNode;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.FieldInsnNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: Coeus
 * @description: 路由信息
 * @author: hu_pf
 * @create: 2021-08-13 20:36
 **/
public class ClassRouter {

    private final Map<String, CoeusClassNode> classRouteMap = new HashMap<>();

    // 处理类的路由
    private static Map<Class, InsnNodeHandler> insnNodeHandlerMap = new HashMap<>();

    // 处理外部类的路由
    private static Map<Class, OuterNodeHandler> outerNodeHandlerMap = new HashMap<>();

    private static ClassRouter classRouter= new ClassRouter();


    public void putClass(String className, ClassNode cn){
        CoeusClassNode coeusClassNode = new CoeusClassNode(className);
        coeusClassNode.initMetadata(cn);
        classRouteMap.put(className,coeusClassNode);
    }

    public CoeusClassNode getClass(String className){
        className = className.replaceAll(Constant.CLASS_SPLIT_POINT,Constant.CLASS_SPLIT_SLASH);
        return classRouteMap.get(className);
    }

    {
        // todo 内部类型处理器要改成动态加载实现类而不是手动加
        MethodInsnNodeHandler methodInsnNodeHandler = new MethodInsnNodeHandler();
        insnNodeHandlerMap.put(methodInsnNodeHandler.getClassType(),methodInsnNodeHandler);

        ParamInsnNodeHandler paramInsnNodeHandler = new ParamInsnNodeHandler();
        insnNodeHandlerMap.put(paramInsnNodeHandler.getClassType(),paramInsnNodeHandler);


        MethodNodeHandler methodNodeHandler = new MethodNodeHandler();
        outerNodeHandlerMap.put(methodNodeHandler.getClassType(),methodNodeHandler);
    }

    private ClassRouter(){

    }

    public static ClassRouter getInstance(){
        return classRouter;
    }

    public void initSubClass(){
        for (CoeusClassNode classNode : this.classRouteMap.values()){
            if (classNode.hasSuperClass()){
                CoeusClassNode superClass = classRouter.getClass(classNode.getInterfaceAndExtendsNames());
                if (superClass != null){
                    superClass.addSubClass(classNode);
                }
            }

            if (classNode.hasInterfaces()){
                List<String> interfaceNames = classNode.getInterfaceNames();
                for (String interfaceName : interfaceNames){
                    CoeusClassNode superClass = classRouter.getClass(interfaceName);
                    if (superClass != null){
                        superClass.addSubClass(classNode);
                    }
                }
            }
        }
        for (CoeusClassNode classNode : this.classRouteMap.values()){
            classNode.initMethodInvokeInfo(classRouter);
        }

    }

}
