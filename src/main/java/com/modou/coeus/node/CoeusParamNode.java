package com.modou.coeus.node;

import com.modou.coeus.common.Constant;
import com.modou.coeus.handler.outerNode.AnnotationNodeHandler;
import jdk.internal.org.objectweb.asm.tree.AnnotationNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: coeus
 * @description: 成员变量节点信息
 * @author: hu_pf
 * @create: 2021-03-08 10:04
 **/
public class CoeusParamNode {

    public String name;

    public String owner;

    public String desc;

    public int access;

    private List<CoeusAnnotationNode> annotationNodes;

    public CoeusParamNode(String name,String desc) {
        this.name = name;
        this.desc = desc;
    }

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
    * @Description: 根据传入的类的名称和参数名称判断是否符合此参数
    * @Param: [className, paraName]
    * @return: boolean
    * @Author: hu_pf
    * @Date: 2023/3/6
    */
    public boolean isConformParam(String className,String paraName){
        className = className.replaceAll(Constant.CLASS_SPLIT_POINT,Constant.CLASS_SPLIT_SLASH);
        return this.owner.equals(className) && this.name.equals(paraName);
    }

    public boolean isEqualClass(String className){
        className = className.replaceAll(Constant.CLASS_SPLIT_POINT,Constant.CLASS_SPLIT_SLASH);
        return this.owner.equals(className);
    }
}
