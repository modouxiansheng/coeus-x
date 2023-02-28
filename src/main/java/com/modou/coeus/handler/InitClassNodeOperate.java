package com.modou.coeus.handler;

import com.modou.coeus.common.NodeHandlerFactory;
import com.modou.coeus.handler.outerNode.AnnotationNodeHandler;
import com.modou.coeus.handler.outerNode.OuterNodeHandler;
import com.modou.coeus.node.CoeusClassNode;
import com.modou.coeus.node.CoeusMethodNode;
import com.modou.coeus.node.CoeusParamNode;
import jdk.internal.org.objectweb.asm.tree.*;

import java.util.List;

/**
 * @program: coeus
 * @description: 初始化类节点的信息
 * @author: hu_pf
 * @create: 2021-03-08 20:38
 **/
public class InitClassNodeOperate implements ClassNodeOperate{
    private static final NodeHandlerFactory nodeHandlerFactory = NodeHandlerFactory.getInstance();

    public void operate(CoeusClassNode classNode) {

        ClassNode cn = classNode.getMetadata();

        List<MethodNode> methods = cn.methods;

        classNode.setSuperName(cn.superName);
        classNode.setInterfaceNames(cn.interfaces);

        classNode.initAnnotationInfo(cn.visibleAnnotations, (AnnotationNodeHandler) nodeHandlerFactory.getOuterNodeHandler(AnnotationNode.class));

        for (FieldNode field : cn.fields) {
            OuterNodeHandler outerNodeHandler = nodeHandlerFactory.getOuterNodeHandler(FieldNode.class);
            CoeusParamNode coeusParamNode = (CoeusParamNode) outerNodeHandler.initialization(field);
            classNode.addParamNode(coeusParamNode);
        }

        for (MethodNode methodNode : methods){
            OuterNodeHandler outerNodeHandler = nodeHandlerFactory.getOuterNodeHandler(MethodNode.class);
            CoeusMethodNode coeusMethodNode = (CoeusMethodNode) outerNodeHandler.initialization(methodNode);
            if (coeusMethodNode == null){
                continue;
            }
            coeusMethodNode.setOwnerClass(classNode.getName());
            classNode.addMethod(coeusMethodNode);
        }
    }
}
