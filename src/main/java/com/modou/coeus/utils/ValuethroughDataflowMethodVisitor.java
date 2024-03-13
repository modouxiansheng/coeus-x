package com.modou.coeus.utils;

import com.modou.coeus.common.ClassRouter;
import com.modou.coeus.node.CoeusClassNode;
import com.modou.coeus.node.CoeusMethodNode;
import com.modou.coeus.node.CoeusParamNode;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;

import java.util.*;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-10-09 18:31
 **/
public class ValuethroughDataflowMethodVisitor extends TaintTrackingMethodVisitor<Integer>{


    private final int access;
    private final String desc;
    private final Set<Integer> returnTaint;//被污染的返回数据

    private Map<CoeusMethodNode,Set<Integer>> passthroughDataflow;

    private ClassRouter classRouter;

    public Set<Integer> getReturnTaint() {
        return returnTaint;
    }

    public ValuethroughDataflowMethodVisitor(int i, int access, String desc, String owner, String name, MethodVisitor mv, Map<CoeusMethodNode,Set<Integer>> passthroughDataflow
    , ClassRouter classRouter) {
        super(i, access, desc,owner,name,mv,passthroughDataflow,classRouter);

        this.access = access;
        this.desc = desc;
        returnTaint = new HashSet<>();
        this.passthroughDataflow = passthroughDataflow;
        this.classRouter = classRouter;
    }

    @Override
    public void visitCode() {
        super.visitCode();

        int localIndex = 0;
        int argIndex = 0;
        if ((this.access & Opcodes.ACC_STATIC) == 0) {
            //非静态方法，第一个局部变量应该为对象实例this
            //添加到本地变量表集合
            setLocalTaint(localIndex, argIndex);
            localIndex += 1;
            argIndex += 1;
        }
        for (Type argType : Type.getArgumentTypes(desc)) {
            //判断参数类型，得出变量占用空间大小，然后存储
            setLocalTaint(localIndex, argIndex);
            localIndex += argType.getSize();
            argIndex += 1;
        }
    }

    @Override
    public void visitInsn(int opcode) {
        switch(opcode) {
            case Opcodes.IRETURN://从当前方法返回int
            case Opcodes.FRETURN://从当前方法返回float
            case Opcodes.ARETURN://从当前方法返回对象引用
                returnTaint.addAll(getStackTaint(0));//栈空间从内存高位到低位分配空间
                break;
            case Opcodes.LRETURN://从当前方法返回long
            case Opcodes.DRETURN://从当前方法返回double
                returnTaint.addAll(getStackTaint(1));
                break;
            case Opcodes.RETURN://从当前方法返回void
                break;
            default:
                break;
        }

        super.visitInsn(opcode);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        switch (opcode) {
            case Opcodes.GETSTATIC:
                break;
            case Opcodes.PUTSTATIC:
                //静态调用没办法污点跟踪
                break;
            case Opcodes.GETFIELD:
                    Type type = Type.getType(desc);//获取字段类型
                    if (type.getSize() == 1) {
                        //size=1可能为引用类型
                        Boolean isTransient = name.equals("<init>");

                        // If a field type could not possibly be serialized, it's effectively transient
                        //判断调用的字段类型是否可序列化
                        //若调用的字段可被序列化，则取当前类实例的所有字段，找出调用的字段，去判断是否被标识了transient
                        CoeusClassNode clazz = classRouter.getClass(owner);
                        while (clazz != null) {

                            //遍历字段，判断是否是transient类型，以确定是否可被序列化
                            CoeusParamNode coeusParamNode = clazz.getCoeusParamNode(name);
                            if (coeusParamNode != null){
                                isTransient = (coeusParamNode.access & Opcodes.ACC_TRANSIENT)!=0;
                            }

                            if (isTransient != null) {
                                break;
                            }
                            //若找不到字段，则向上父类查找，继续遍历
                            clazz = classRouter.getClass(clazz.getInterfaceAndExtendsNames());
                        }

                        Set<Integer> taint;
                        if (!Boolean.TRUE.equals(isTransient)) {
                            //若不是Transient字段，则从栈顶取出它，取出的是this或某实例变量，即字段所属实例
                            taint = getStackTaint(0);
                        } else {
                            taint = new HashSet<>();
                        }

                        super.visitFieldInsn(opcode, owner, name, desc);
                        setStackTaint(0, taint);
                        return;
                    }
                break;
            case Opcodes.PUTFIELD:
                System.out.println(owner+"----"+getName() +"======"+desc +":"+name + ":" + getStackTaint(0));

                break;
            default:
                throw new IllegalStateException("Unsupported opcode: " + opcode);
        }

        super.visitFieldInsn(opcode, owner, name, desc);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        //获取method参数类型
        Type[] argTypes = Type.getArgumentTypes(desc);
        if (opcode != Opcodes.INVOKESTATIC) {
            //如果执行的非静态方法，则把数组第一个元素类型设置为该实例对象的类型，类比局部变量表
            Type[] extendedArgTypes = new Type[argTypes.length+1];
            System.arraycopy(argTypes, 0, extendedArgTypes, 1, argTypes.length);
            extendedArgTypes[0] = Type.getObjectType(owner);
            argTypes = extendedArgTypes;
        }
        //获取返回值类型大小
        int retSize = Type.getReturnType(desc).getSize();

        Set<Integer> resultTaint;
        switch (opcode) {
            case Opcodes.INVOKESTATIC://调用静态方法
            case Opcodes.INVOKEVIRTUAL://调用实例方法
            case Opcodes.INVOKESPECIAL://调用超类构造方法，实例初始化方法，私有方法
            case Opcodes.INVOKEINTERFACE://调用接口方法
                //todo 1 构造污染参数集合，方法调用前先把操作数入栈
                final List<Set<Integer>> argTaint = new ArrayList<Set<Integer>>(argTypes.length);
                for (int i = 0; i < argTypes.length; i++) {
                    argTaint.add(null);
                }

                int stackIndex = 0;
                for (int i = 0; i < argTypes.length; i++) {
                    Type argType = argTypes[i];
                    if (argType.getSize() > 0) {
                        //根据参数类型大小，从栈顶获取入参，参数入栈是从左到右的
                        argTaint.set(argTypes.length - 1 - i, getStackTaint(stackIndex + argType.getSize() - 1));
                    }
                    stackIndex += argType.getSize();
                }

                //todo 2 构造方法的调用，意味参数0可以污染返回值
                if (name.equals("<init>")) {
                    // Pass result taint through to original taint set; the initialized object is directly tainted by
                    // parameters
                    resultTaint = argTaint.get(0);
                } else {
                    resultTaint = new HashSet<>();
                }

                //todo 3 前面已做逆拓扑，调用链最末端最先被visit，因此，调用到的方法必然已被visit分析过
//                new MethodReference.Handle(new ClassReference.Handle(owner), name, desc);
                CoeusMethodNode method = classRouter.getClass(owner).getMethod(name, desc);
                Set<Integer> passthrough = this.passthroughDataflow.get(method);
                if (passthrough != null) {
                    for (Integer passthroughDataflowArg : passthrough) {
                        //判断是否和同一方法体内的其它方法返回值关联，有关联则添加到栈底，等待执行return时保存
                        resultTaint.addAll(argTaint.get(passthroughDataflowArg));
                    }
                }

                break;
            default:
                throw new IllegalStateException("Unsupported opcode: " + opcode);
        }

        super.visitMethodInsn(opcode, owner, name, desc, itf);

        //只有返回值大于0，表示存在返回值，这样才能污点传播下去
        if (retSize > 0) {
            getStackTaint(retSize-1).addAll(resultTaint);
        }
    }
}
