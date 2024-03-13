package com.modou.coeus.node;

import com.modou.coeus.common.ClassRouter;
import com.modou.coeus.handler.ClassNodeOperate;
import com.modou.coeus.handler.outerNode.AnnotationNodeHandler;
import jdk.internal.org.objectweb.asm.tree.AnnotationNode;
import jdk.internal.org.objectweb.asm.tree.ClassNode;

import java.io.InputStream;
import java.util.*;

/**
 * @program: coeus
 * @description: 类节点信息
 * @author: hu_pf
 * @create: 2021-03-08 10:02
 **/
public class CoeusClassNode {

    // 包信息
    private String packageInfo;

    private String name;

    // 全路径名
    private String allPath;

    // 别名
    private String alias;

    // 继承类信息
    private String superName;

    private boolean hasSuperClass;
    // 接口类信息
    private List<String> interfaceNames;


    // 类描述信息 具体值请看 ClassTypeConstant
    private int classType;

    // 类中包含的方法
    public List<CoeusMethodNode> methods;

    // id 和方法的路由关系
    private Map<String,CoeusMethodNode> routIdAndMethodMap = new HashMap<>();

    // 名称和方法的路由
    private Map<String,CoeusMethodNode> routNameAndMethodMap = new HashMap<>();

    private Map<String,Integer> routNameAndMethodMapCount = new HashMap<>();

    // 类中包含的注解
    private List<CoeusAnnotationNode> annotationNodes;

    // 类中包含的成员变量
    private List<CoeusParamNode> coeusParamNodes = new ArrayList<>();

    private ClassNode metaData;

    private List<CoeusClassNode> subClass = new ArrayList<>();

    private InputStream inputStream;

    public CoeusClassNode(String name){
        this.name = name;
    }

    public CoeusClassNode(){
    }

    /**
    * @Description: 添加方法
    * @Param: [coeusMethodNode]
    * @return: void
    * @Author: hu_pf
    * @Date: 2021/8/13
    */
    public void addMethod(CoeusMethodNode coeusMethodNode){
        if (methods == null){
            methods = new ArrayList<>();
        }
        routIdAndMethodMap.put(coeusMethodNode.getId(), coeusMethodNode);

        routNameAndMethodMap.put(coeusMethodNode.getName(), coeusMethodNode);

        Integer count = routNameAndMethodMapCount.getOrDefault(coeusMethodNode.getName(), 0);
        routNameAndMethodMapCount.put(coeusMethodNode.getName(),count+1);
        methods.add(coeusMethodNode);
    }

    public void initMetadata(ClassNode classNode, InputStream inputStream){
        this.metaData = classNode;
        this.inputStream = inputStream;

    }

    public ClassNode getMetadata(){
        return metaData;
    }


    public CoeusMethodNode getMethod(String name){
        CoeusMethodNode coeusMethodNode = routNameAndMethodMap.get(name);
        if (coeusMethodNode == null && hasSuperClass){
            CoeusClassNode aClass = ClassRouter.getInstance().getClass(this.superName);
            if (aClass != null){
                coeusMethodNode = aClass.getMethod(name);
            }
        }
        return coeusMethodNode;
    }

    public CoeusMethodNode getMethod(String name,String desc){
        CoeusMethodNode coeusMethodNode = routIdAndMethodMap.get(CoeusMethodNode.generateId(name, desc));
        if (coeusMethodNode == null && hasSuperClass){
            CoeusClassNode aClass = ClassRouter.getInstance().getClass(this.superName);
            if (aClass != null){
                coeusMethodNode = aClass.getMethod(name,desc);
            }
        }
        return coeusMethodNode;
    }

    public void setSuperName(String superName){
        this.superName = superName;
        this.hasSuperClass = true;
    }

    public void setInterfaceNames(List<String> interfaceNames){
        this.interfaceNames = interfaceNames;
    }

    public boolean hasParamTer(String name){
        for (CoeusParamNode param : coeusParamNodes) {
            if (param.name.equals(name)){
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public CoeusParamNode getCoeusParamNode(String name){
        for (CoeusParamNode param : coeusParamNodes) {
            if (param.name.equals(name)){
                return param;
            }
        }
        return null;
    }

    public boolean hasSuperClass(){
        return hasSuperClass;
    }

    public String getInterfaceAndExtendsNames(){
        return superName;
    }

    public boolean hasInterfaces(){
        return interfaceNames != null && interfaceNames.size()!=0;
    }

    public List<String> getInterfaceNames(){
        return interfaceNames;
    }


    public void initMethodInvokeInfo(ClassRouter classRouter){
        if (this.methods == null || this.methods.isEmpty()){
            return;
        }
        for (CoeusMethodNode coeusMethodNode : this.methods) {
            if (coeusMethodNode.invokeInfos == null || coeusMethodNode.invokeInfos.isEmpty()){
                continue;
            }

            for (int i = 0; i < coeusMethodNode.invokeInfos.size(); i++) {
                String invokeInfo = coeusMethodNode.invokeInfos.get(i);
                String[] split = invokeInfo.split("#");
                String className = split[0];
                String methodName = split[1];
                String desc = split[2];

                CoeusClassNode aClass = classRouter.getClass(className);
                if (aClass!= null){
                    if (aClass.hasSuperClass){
                        for (CoeusClassNode classNode : aClass.subClass) {
                            if (!classNode.metaData.name.equals(className)){
                                coeusMethodNode.addInvokeMethodInfo(classNode.name,methodName,desc);
                            }
                        }
                    }
                }
            }
        }
    }


    public void addParamNode(CoeusParamNode coeusParamNodes){
        this.coeusParamNodes.add(coeusParamNodes);
    }

    /**
    * @Description: 操作数据
    * @Param: [classNodeOperate]
    * @return: void
    * @Author: hu_pf
    * @Date: 2021/3/8
    */
    public void visit(ClassNodeOperate classNodeOperate){
        classNodeOperate.operate(this);
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

    /**
    * @Description: 添加子类信息
    * @Param: [coeusClassNode]
    * @return: void
    * @Author: hu_pf
    * @Date: 2023/2/27
    */
    public void addSubClass(CoeusClassNode coeusClassNode){
        if (subClass == null || subClass.isEmpty()){
            subClass = new ArrayList<>();
        }
        subClass.add(coeusClassNode);
    }

    public String getName() {
        return name;
    }
}
