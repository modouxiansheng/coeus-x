package com.modou.coeus.handler.innerNode;

import com.modou.coeus.node.CoeusMethodNode;
import com.modou.coeus.node.CoeusParamNode;
import jdk.internal.org.objectweb.asm.tree.FieldInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;

/**
 * @program: Coeus
 * @description: 内部方法节点的处理类
 * @author: hu_pf
 * @create: 2021-08-13 21:31
 **/
public class ParamInsnNodeHandler extends AbstractInsnNodeHandler<FieldInsnNode> {

    @Override
    public void doInvoke(FieldInsnNode fieldInsnNode, CoeusMethodNode coeusMethodNode) {
        coeusMethodNode.coeusParamNodes.add(new CoeusParamNode(fieldInsnNode.name, fieldInsnNode.owner));
    }

    @Override
    public Class getClassType() {
        return FieldInsnNode.class;
    }
}
