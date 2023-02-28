package com.modou.coeus.handler.innerNode;

import com.modou.coeus.handler.innerNode.AbstractInsnNodeHandler;
import com.modou.coeus.node.CoeusMethodNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;

/**
 * @program: Coeus
 * @description: 内部方法节点的处理类
 * @author: hu_pf
 * @create: 2021-08-13 21:31
 **/
public class MethodInsnNodeHandler extends AbstractInsnNodeHandler<MethodInsnNode> {

    @Override
    public void doInvoke(MethodInsnNode methodInsnNode, CoeusMethodNode coeusMethodNode) {

        coeusMethodNode.addInvokeMethodInfo(methodInsnNode.owner,methodInsnNode.name,methodInsnNode.desc);

    }

    @Override
    public Class getClassType() {
        return MethodInsnNode.class;
    }
}
