package com.modou.coeus;

import com.modou.coeus.ability.data.AnnotationForValueData;
import com.modou.coeus.common.ClassRouter;
import com.modou.coeus.node.CoeusClassNode;
import com.modou.coeus.node.CoeusMethodNode;
import com.modou.coeus.node.CoeusParamNode;
import jdk.internal.org.objectweb.asm.tree.AnnotationNode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * @program: coeus
 * @description: 节点初始化
 * @author: hu_pf
 * @create: 2021-03-08 20:48
 **/
public class NodeFacade {

    public static final String IGNORE_GIT = ".git";
    public static final String IGNORE_IDEA = ".idea";
    public static final String IGNORE_CLASS = ".class";

    public static void main(String[] args) {

    }

    public static void parseInvoke(CoeusMethodNode invoke,ClassRouter instance){

        if (invoke == null || invoke.invokeInfos == null){
            return;
        }

        Stack<CoeusMethodNode> stack = new Stack<>();

        Set<String> coeusMethodNodeSet = new HashSet<>();

        stack.add(invoke);
        coeusMethodNodeSet.add(invoke.getOwnerClass()+invoke.getId());

        Set<AnnotationForValueData> annotationForValueData = new HashSet<>();
        while (!stack.isEmpty()){
            CoeusMethodNode curr = stack.pop();

            for (String invokeInfo : curr.invokeInfos) {
                String[] split = invokeInfo.split("#");
                String className = split[0];
                String methodName = split[1];
                String desc = split[2];
                CoeusClassNode aClass = instance.getClass(className);
                if (aClass == null){
                    continue;
                }
                CoeusMethodNode next = aClass.getMethod(methodName,desc);
                if (next != null){
                    Set<AnnotationForValueData> valueData = validParamAnnotion(next, instance);
                    if (valueData != null && !valueData.isEmpty()){
                        annotationForValueData.addAll(valueData);
                    }
                    if (!coeusMethodNodeSet.contains(next.getOwnerClass()+next.getId())){
                        stack.add(curr);
                        stack.add(next);
                        coeusMethodNodeSet.add(next.getOwnerClass()+next.getId());
                    }
                }
            }

        }

        annotationForValueData.stream().forEach(e-> {
            String value = e.getValue();
            System.out.println(e.getName() + ":" + value);
        });

    }

    public static Set<AnnotationForValueData> validParamAnnotion(CoeusMethodNode invoke, ClassRouter instance){
        if (invoke == null || invoke.coeusParamNodes == null){
            return null;
        }
        Set<AnnotationForValueData> valueData = new HashSet<>();
        for (CoeusParamNode coeusParamNode : invoke.coeusParamNodes) {
            CoeusClassNode aClass1 = instance.getClass(coeusParamNode.owner);
            if (aClass1 != null && aClass1.hasParamTer(coeusParamNode.name)){
                // 参数上有@Value
                CoeusParamNode coeusParamNode1 = aClass1.getCoeusParamNode(coeusParamNode.name);
                if (coeusParamNode1.containAnnotationName("Value")){
                    printValue(invoke.getId(),coeusParamNode1.name);
                    valueData.add(new AnnotationForValueData(coeusParamNode1.name,coeusParamNode1.getCoeusAnnotationNodeByName("Value").getStringValue("value")));
                }

                // 方法上有@Value
                if (aClass1.methods != null){
                    for (CoeusMethodNode method : aClass1.methods) {
                        if (method.getName().startsWith("set")){
                            if (method.coeusParamNodes != null){
                                for (CoeusParamNode paramNode : method.coeusParamNodes) {
                                    if (paramNode.name.equals(coeusParamNode.name) && method.containAnnotationName("Value")){
                                        printValue(invoke.getId(),coeusParamNode1.name);
                                        valueData.add(new AnnotationForValueData(coeusParamNode1.name,method.getCoeusAnnotationNodeByName("Value").getStringValue("value")));
                                    }
                                }
                            }
                        }
                    }
                }

                // 类上面有@ConfigurationProperties
                if (aClass1.containAnnotationName("ConfigurationProperties")){
                    printValue(invoke.getId(),coeusParamNode1.name);
                    valueData.add(new AnnotationForValueData(coeusParamNode1.name,aClass1.getCoeusAnnotationNodeByName("ConfigurationProperties").getStringValue("prefix")));
                }

                // 是GrayArkUtils 这个类的

            }
        }

        return valueData;
    }

    private static void printValue(String id,String name){
//        System.out.println(name);
    }



    //传入一个工程路径
    //搜索下面的class
    //读成流,发给解析类
    public static void buildSource(String projectRoot) {
        File root = new File(projectRoot);
        if (!root.exists()) {
            return;
        }
        if (root.isDirectory()) {
            String path = root.getAbsolutePath();
            if (!path.endsWith(IGNORE_GIT) || !path.endsWith(IGNORE_IDEA)) {
                //递归
                for (String child : root.list()) {
                    buildSource(path + File.separator + child);
                }
            }
        } else {
            if (root.getAbsolutePath().endsWith(IGNORE_CLASS)) {
                try {
                    FileInputStream in = new FileInputStream(root);
                    ParseClass.parseSourceClass(in);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
