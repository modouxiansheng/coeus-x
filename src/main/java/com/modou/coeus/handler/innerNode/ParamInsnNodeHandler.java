package com.modou.coeus.handler.innerNode;

import com.modou.coeus.node.CoeusMethodNode;
import com.modou.coeus.node.CoeusParamNode;
import jdk.internal.org.objectweb.asm.Opcodes;
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
        CoeusParamNode coeusParamNode = new CoeusParamNode(fieldInsnNode.name, fieldInsnNode.desc);
        coeusParamNode.owner = fieldInsnNode.owner;
        coeusMethodNode.coeusParamNodes.add(coeusParamNode);
    }

    @Override
    public Class getClassType() {
        return FieldInsnNode.class;
    }
}
