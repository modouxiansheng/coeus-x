package com.modou.coeus.handler.innerNode;

import com.modou.coeus.node.CoeusMethodNode;
import jdk.internal.org.objectweb.asm.Handle;
import jdk.internal.org.objectweb.asm.tree.InvokeDynamicInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;

/**
 * @program: Coeus
 * @description: 内部方法节点的处理类
 * @author: hu_pf
 * @create: 2021-08-13 21:31
 **/
public class InvokeDynamicInsnNodeHandler extends AbstractInsnNodeHandler<InvokeDynamicInsnNode> {

    @Override
    public void doInvoke(InvokeDynamicInsnNode invokeDynamicInsnNode, CoeusMethodNode coeusMethodNode) {
        for (Object bsmArg : invokeDynamicInsnNode.bsmArgs) {
            if (bsmArg instanceof Handle){
                String name = ((Handle) bsmArg).getName();
                String owner = ((Handle) bsmArg).getOwner();
                String desc = ((Handle) bsmArg).getDesc();
                coeusMethodNode.addInvokeMethodInfo(owner,name,desc);
            }
        }

//        coeusMethodNode.addInvokeMethodInfo(methodInsnNode.owner,methodInsnNode.name,methodInsnNode.desc);

    }

    @Override
    public Class getClassType() {
        return InvokeDynamicInsnNode.class;
    }
}
