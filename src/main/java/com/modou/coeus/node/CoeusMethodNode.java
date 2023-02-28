package com.modou.coeus.node;

import com.modou.coeus.common.Constant;
import com.modou.coeus.handler.innerNode.InsnNodeHandler;
import com.modou.coeus.handler.outerNode.AnnotationNodeHandler;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.AnnotationNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: coeus
 * @description: 方法节点信息
 * @author: hu_pf
 * @create: 2021-03-08 10:02
 **/
public class CoeusMethodNode {

    private String ownerClass;

    // 全路径名
    private String allPath;

    private String id;

    // 别名
    private String name;

    // 描述：入参和出参，可以在方法中唯一确认到一个方法
    private String desc;

    // 入参集合 全路径名描述
    private List<String> requestParams;

    // 出参 全路径名描述
    private String returnParam;

    // 访问权限 具体描述值请看 AccessConstant
    private Integer access;

    // 抛出的异常信息 记录全路径名
    private List<String> throwExceptions;
    
    public List<String> invokeInfos = new ArrayList<>();

    public List<CoeusParamNode> coeusParamNodes = new ArrayList<>();

    private List<CoeusAnnotationNode> annotationNodes;

    public CoeusMethodNode(){

    }

    public CoeusMethodNode(String name,String desc){
        this.name = name;
        this.desc = desc;
        this.id = generateId(name,desc);
    }
    
    /**
    * @Description: 添加执行的方法信息
    * @Param: [className, methodName, desc]
    * @return: void
    * @Author: hu_pf
    * @Date: 2021/8/13
    */
    public void addInvokeMethodInfo(String className,String methodName,String desc){
        if (invokeInfos == null){
            invokeInfos = new ArrayList<>();
        }
        invokeInfos.add(className + Constant.SPLIT + methodName + Constant.SPLIT + desc);
    }


    public void visit(InsnNodeHandler insnNodeHandler,AbstractInsnNode abstractInsnNode){
        insnNodeHandler.invoke(abstractInsnNode,this);
    }

    public String getId(){

        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public static String generateId(String name,String desc){
        return name + Constant.SPLIT + desc;
    }


    /**
    * @Description: 初始化方法上的注解信息
    * @Param: [list, annotationNodeHandler]
    * @return: void
    * @Author: hu_pf
    * @Date: 2023/2/27
    */
    public void initAnnotationInfo(List<AnnotationNode> list, AnnotationNodeHandler annotationNodeHandler){
        if (list != null){
            if (annotationNodes == null){
                annotationNodes = new ArrayList<>();
            }
            for (AnnotationNode visibleAnnotation : list) {
                annotationNodes.add(annotationNodeHandler.initialization(visibleAnnotation));
            }
        }
    }

    /**
    * @Description: 判断方法上是否包含某个注解
    * @Param: [name]
    * @return: boolean
    * @Author: hu_pf
    * @Date: 2023/2/27
    */
    public boolean containAnnotationName(String name){
        if (annotationNodes == null || annotationNodes.isEmpty()){
            return Boolean.FALSE;
        }
        for (CoeusAnnotationNode annotationNode : annotationNodes) {
            if (annotationNode.getName().contains(name)){
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public CoeusAnnotationNode getCoeusAnnotationNodeByName(String name){
        for (CoeusAnnotationNode annotationNode : annotationNodes) {
            if (annotationNode.getName().contains(name)){
                return annotationNode;
            }
        }
        return null;
    }

    public void setOwnerClass(String ownerClass) {
        this.ownerClass = ownerClass;
    }

    public String getOwnerClass() {
        return ownerClass;
    }
}
