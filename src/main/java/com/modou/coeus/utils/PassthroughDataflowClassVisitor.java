package com.modou.coeus.utils;

import com.modou.coeus.common.ClassRouter;
import com.modou.coeus.node.CoeusMethodNode;
import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.commons.JSRInlinerAdapter;

import java.util.Map;
import java.util.Set;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-10-09 20:34
 **/
public class PassthroughDataflowClassVisitor extends ClassVisitor {

    private String name;

    private PassthroughDataflowMethodVisitor passthroughDataflowMethodVisitor;

    private CoeusMethodNode coeusMethodNode;

    private final Map<CoeusMethodNode, Set<Integer>> passthroughDataflow;

    private ClassRouter classRouter;


    public PassthroughDataflowClassVisitor(int i, CoeusMethodNode coeusMethodNode,Map<CoeusMethodNode, Set<Integer>> passthroughDataflow,ClassRouter classRouter) {
        super(i);
        this.coeusMethodNode = coeusMethodNode;
        this.passthroughDataflow = passthroughDataflow;
        this.classRouter = classRouter;
    }

    @Override
    public void visit(int version, int access, String name, String signature,
                      String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.name = name;

    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc,
                                     String signature, String[] exceptions) {

        //不是目标观察的method需要跳过，上一步得到的method都是有调用关系的method才需要数据流分析
        if (!name.equals(coeusMethodNode.getName()) ||  !desc.equals(coeusMethodNode.getDesc())) {
            return null;
        }
        if (passthroughDataflowMethodVisitor != null) {
            throw new IllegalStateException("Constructing passthroughDataflowMethodVisitor twice!");
        }

        //对method进行观察
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        passthroughDataflowMethodVisitor = new PassthroughDataflowMethodVisitor(Opcodes.ASM5,access,desc,this.name,name,mv,this.passthroughDataflow,this.classRouter);

        return new JSRInlinerAdapter(passthroughDataflowMethodVisitor, access, name, desc, signature, exceptions);
    }

    public Set<Integer> getReturnTaint() {
        if (passthroughDataflowMethodVisitor == null) {
            throw new IllegalStateException("Never constructed the passthroughDataflowmethodVisitor!");
        }
        return passthroughDataflowMethodVisitor.getReturnTaint();
    }
}
