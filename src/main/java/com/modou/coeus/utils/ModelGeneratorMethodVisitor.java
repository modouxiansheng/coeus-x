package com.modou.coeus.utils;

import com.modou.coeus.ability.scan.ScanCallHandlerForParamBacktrack;
import com.modou.coeus.common.ClassRouter;
import com.modou.coeus.node.CoeusClassNode;
import com.modou.coeus.node.CoeusMethodNode;
import com.modou.coeus.node.CoeusParamNode;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-10-12 18:37
 **/
public class ModelGeneratorMethodVisitor extends TaintTrackingMethodVisitor<String>{

    private ClassRouter classRouter;

    private final Map<CoeusMethodNode, Set<Integer>> passthroughDataflow;

    private final String owner;
    private final int access;
    private final String name;
    private final String desc;



    public ModelGeneratorMethodVisitor(ClassRouter classRouter,
                                       Map<CoeusMethodNode, Set<Integer>> passthroughDataflow,
                                       final int api, final MethodVisitor mv,
                                       final String owner, int access, String name, String desc) {
        super(api, access, desc,owner,name,mv,passthroughDataflow,classRouter);
        this.classRouter = classRouter;
        this.owner = owner;
        this.access = access;
        this.name = name;
        this.desc = desc;
        this.passthroughDataflow = passthroughDataflow;
    }

    @Override
    public void visitCode() {
        super.visitCode();

        int localIndex = 0;
        int argIndex = 0;
        //使用arg前缀来表示方法入参，后续用于判断是否为目标调用方法的入参
        if ((this.access & Opcodes.ACC_STATIC) == 0) {
            setLocalTaint(localIndex, "arg" + argIndex);
            localIndex += 1;
            argIndex += 1;
        }
        for (Type argType : Type.getArgumentTypes(desc)) {
            setLocalTaint(localIndex, "arg" + argIndex);
            localIndex += argType.getSize();
            argIndex += 1;
        }
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {

        switch (opcode) {
            case Opcodes.GETSTATIC:
                break;
            case Opcodes.PUTSTATIC:
                break;
            case Opcodes.GETFIELD://入操作栈
                Type type = Type.getType(desc);
                if (type.getSize() == 1) {
                    Boolean isTransient = null;

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

                    Set<String> newTaint = new HashSet<>();
                    if (!Boolean.TRUE.equals(isTransient)) {
                        for (String s : getStackTaint(0)) {
                            newTaint.add(s + "." + name);
                        }
                    }
                    super.visitFieldInsn(opcode, owner, name, desc);
                    //在调用方法前，都会先入栈，作为参数
                    setStackTaint(0, newTaint);
                    return;
                }
                break;
            case Opcodes.PUTFIELD:
                break;
            default:
                throw new IllegalStateException("Unsupported opcode: " + opcode);
        }

        super.visitFieldInsn(opcode, owner, name, desc);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        //获取被调用method的参数和类型，非静态方法需要把实例类型放在第一个元素
        Type[] argTypes = Type.getArgumentTypes(desc);
        if (opcode != Opcodes.INVOKESTATIC) {
            Type[] extendedArgTypes = new Type[argTypes.length+1];
            System.arraycopy(argTypes, 0, extendedArgTypes, 1, argTypes.length);
            extendedArgTypes[0] = Type.getObjectType(owner);
            argTypes = extendedArgTypes;
        }

        switch (opcode) {
            case Opcodes.INVOKESTATIC:
            case Opcodes.INVOKEVIRTUAL:
            case Opcodes.INVOKESPECIAL:
            case Opcodes.INVOKEINTERFACE:
//                if (!ConfigHelper.taintTrack) {
//                    //不进行污点分析，全部调用关系都记录
//                    discoveredCalls.add(new GraphCall(
//                            new MethodReference.Handle(new ClassReference.Handle(this.owner), this.name, this.desc),
//                            new MethodReference.Handle(new ClassReference.Handle(owner), name, desc),
//                            0,
//                            "",
//                            0));
//                    break;
//                }
                int stackIndex = 0;
                for (int i = 0; i < argTypes.length; i++) {
                    //最右边的参数，就是最后入栈，即在栈顶
                    int argIndex = argTypes.length-1-i;
                    Type type = argTypes[argIndex];
                    //操作数栈出栈，调用方法前，参数都已入栈
                    Set<String> taint = getStackTaint(stackIndex);
                    if (taint.size() > 0) {
                        for (String argSrc : taint) {
                            //取出出栈的参数，判断是否为当前方法的入参，arg前缀
                            if (!argSrc.substring(0, 3).equals("arg")) {
                                throw new IllegalStateException("Invalid taint arg: " + argSrc);
                            }
                            int dotIndex = argSrc.indexOf('.');
                            int srcArgIndex;
                            String srcArgPath;
                            if (dotIndex == -1) {
                                srcArgIndex = Integer.parseInt(argSrc.substring(3));
                                srcArgPath = null;
                            } else {
                                srcArgIndex = Integer.parseInt(argSrc.substring(3, dotIndex));
                                srcArgPath = argSrc.substring(dotIndex+1);
                            }
                            //记录参数流动关系
                            //argIndex：当前方法参数索引，srcArgIndex：对应上一级方法的参数索引
                            graphCall(
                                    new Handle(this.owner,this.name, this.desc),
                                    new Handle(owner,name,desc),
                                    srcArgIndex,
                                    srcArgPath,
                                    argIndex);

                        }
                    }

                    stackIndex += type.getSize();
                }
                break;
            default:
                throw new IllegalStateException("Unsupported opcode: " + opcode);
        }

        super.visitMethodInsn(opcode, owner, name, desc, itf);
    }

    private void graphCall(Handle source,Handle target,int srcArgIndex,String srcArgPath,int argIndex){
//        System.out.println(source.toString() + "->" + target.toString());
//        System.out.println(srcArgIndex);
//        System.out.println(srcArgPath);
//        System.out.println(argIndex);
        ScanCallHandlerForParamBacktrack.discoveredCalls.add(new GraphCall(
                source,
                target,
                srcArgIndex,
                srcArgPath,
                argIndex));
    }


    public static class Handle{
        private String owner;
        private String name;
        private String desc;

        public Handle(String owner, String name, String desc) {
            this.owner = owner;
            this.name = name;
            this.desc = desc;
        }

        @Override
        public String toString() {
            return "Handle{" +
                    "owner='" + owner + '\'' +
                    ", name='" + name + '\'' +
                    ", desc='" + desc + '\'' +
                    '}';
        }

        public String getOwner() {
            return owner;
        }

        public String getName() {
            return name;
        }

        public String getDesc() {
            return desc;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Handle handle = (Handle) o;

            if (owner != null ? !owner.equals(handle.owner) : handle.owner != null) return false;
            if (name != null ? !name.equals(handle.name) : handle.name != null) return false;
            return desc != null ? desc.equals(handle.desc) : handle.desc == null;
        }

        @Override
        public int hashCode() {
            int result = (owner != null ? owner.hashCode() : 0);
            result = 31 * result + (name != null ? name.hashCode() : 0);
            result = 31 * result + (desc != null ? desc.hashCode() : 0);
            return result;
        }
    }
}
