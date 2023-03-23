package com.modou.coeus.parse.scan.backtrack;

import com.modou.coeus.ability.scan.ScanCallChainAbility;
import com.modou.coeus.ability.scan.ScanCallHandlerForParamCallChain;
import com.modou.coeus.common.ClassRouter;
import com.modou.coeus.node.CoeusClassNode;
import com.modou.coeus.node.CoeusMethodNode;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.tree.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: coeus-x
 * @description: 数据源
 * @author: hu_pf
 * @create: 2023-03-06 20:00
 **/
public class MetaDataMain {

    public static void main(String[] args) {
        String projectRoot = "/Users/admin/mygit/coeus-x/target/test-classes/com/modou/coeus/parse/scan/backtrack";
        ScanCallHandlerForParamCallChain scanCallHandlerForParamCallChain = new ScanCallHandlerForParamCallChain();
        ScanCallChainAbility scanCallChainAbility = new ScanCallChainAbility(projectRoot,scanCallHandlerForParamCallChain);
        scanCallChainAbility.invoke("com.modou.coeus.parse.scan.backtrack.MetaData","getName");

        ClassRouter classRouter = ClassRouter.getInstance();
        CoeusClassNode aClass = classRouter.getClass("com.modou.coeus.parse.scan.backtrack.MetaData");
        CoeusMethodNode method = aClass.getMethod("invoke");
        Map<String, Object> buildName = MethodCallAnalyzer.analyzeMethodCall(method.getMetaData(), "buildName");
        System.out.println("1");
    }




    private static boolean isConstParameter(AbstractInsnNode insnNode) {
        if (insnNode instanceof LdcInsnNode) {
            return true;
        } else if (insnNode instanceof VarInsnNode) {
            int opcode = insnNode.getOpcode();
            return opcode >= Opcodes.ILOAD && opcode <= Opcodes.ALOAD;
        } else {
            return false;
        }
    }

    public static List<String> findVariableOrConstParameter(MethodNode methodNode, String owner, String methodName) {
        List<String> results = new ArrayList<>();
        Map<Integer, String> varMap = new HashMap<>();

        // build the variable map
        for (LocalVariableNode localVar : methodNode.localVariables) {
            varMap.put(localVar.index, localVar.name);
        }
        ListIterator<AbstractInsnNode> iterator = methodNode.instructions.iterator();
        while (iterator.hasNext()){
            AbstractInsnNode insnNode = iterator.next();
            if (insnNode instanceof MethodInsnNode) {
                MethodInsnNode methodInsnNode = (MethodInsnNode) insnNode;
                if (methodName.equals(methodInsnNode.name)) {
                    // iterate over the method invocation's parameters
                    for (int i = 0; i < methodInsnNode.desc.length(); i++) {
                        char c = methodInsnNode.desc.charAt(i);
                        if (c == 'L' || c == '[') {
                            // object or array type
                            int endIndex = methodInsnNode.desc.indexOf(';', i) + 1;
                            i = endIndex - 1;
                        } else if (c == 'I' || c == 'Z' || c == 'S' || c == 'B' || c == 'C') {
                            // int, boolean, short, byte, or char type
                            AbstractInsnNode paramNode = methodInsnNode.getPrevious();
                            while (paramNode != null && !isConstParameter(paramNode) && paramNode instanceof VarInsnNode) {
                                VarInsnNode varInsnNode = (VarInsnNode) paramNode;
                                String varName = varMap.get(varInsnNode.var);
                                if (varName != null) {
                                    results.add(varName);
                                }
                                paramNode = paramNode.getPrevious();
                            }
                            if (paramNode instanceof LdcInsnNode) {
                                LdcInsnNode ldcInsnNode = (LdcInsnNode) paramNode;
                                results.add(ldcInsnNode.cst.toString());
                            }
                        } else if (c == 'J' || c == 'D') {
                            // long or double type
                            i++;
                        } else {
                            throw new IllegalArgumentException("Unexpected parameter type: " + c);
                        }
                    }
                }
            }
        }

        return results;
    }


}
