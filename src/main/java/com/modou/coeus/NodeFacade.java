package com.modou.coeus;

import com.modou.coeus.ability.data.AnnotationForValueData;
import com.modou.coeus.ability.scan.ScanCallChainAbility;
import com.modou.coeus.ability.scan.ScanCallHandlerForValueAnnotation;
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
        String projectRoot = "xxx";
        ScanCallHandlerForValueAnnotation scanCallHandlerForValueAnnotation = new ScanCallHandlerForValueAnnotation();
        ScanCallChainAbility scanCallChainAbility = new ScanCallChainAbility(projectRoot,scanCallHandlerForValueAnnotation);
        scanCallChainAbility.invoke("xxx","xxx");
        scanCallHandlerForValueAnnotation.getValueData().forEach(e-> System.out.println(e.getName()));
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
