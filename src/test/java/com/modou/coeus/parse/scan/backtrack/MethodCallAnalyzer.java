package com.modou.coeus.parse.scan.backtrack;

import jdk.internal.org.objectweb.asm.tree.*;

import java.util.*;

public class MethodCallAnalyzer {

    public static Map<String, Object> analyzeMethodCall(MethodNode methodNode, String targetMethodName) {
        Map<String, Object> results = new HashMap<>();
        List<AbstractInsnNode> instructions = Arrays.asList(methodNode.instructions.toArray());

        for (AbstractInsnNode insn : instructions) {
            if (insn instanceof MethodInsnNode) {
                MethodInsnNode methodInsnNode = (MethodInsnNode) insn;
                if (methodInsnNode.name.equals(targetMethodName)) {
                    Map<String, Object> params = new HashMap<>();
                    TypeInsnNode newInstanceInsnNode = (TypeInsnNode) instructions.get(instructions.indexOf(methodInsnNode) - 1);
                    ListIterator<AbstractInsnNode> iterator = instructions.listIterator(instructions.indexOf(newInstanceInsnNode));
                    while (iterator.hasNext()) {
                        AbstractInsnNode paramInsn = iterator.next();
                        if (paramInsn instanceof VarInsnNode) {
                            VarInsnNode varInsnNode = (VarInsnNode) paramInsn;
                            int varIndex = varInsnNode.var;
                            params.put("var" + varIndex, getParamSource(instructions, varIndex, iterator.previousIndex()));
                        } else if (paramInsn instanceof FieldInsnNode) {
                            FieldInsnNode fieldInsnNode = (FieldInsnNode) paramInsn;
                            params.put(fieldInsnNode.name, getParamSource(instructions, fieldInsnNode, iterator.previousIndex()));
                        } else if (paramInsn instanceof LdcInsnNode) {
                            LdcInsnNode ldcInsnNode = (LdcInsnNode) paramInsn;
                            params.put(ldcInsnNode.cst.toString(), "literal");
                        } else {
                            break;
                        }
                    }
                    results.put("params", params);
                    break;
                }
            }
        }

        return results;
    }

    private static String getParamSource(List<AbstractInsnNode> instructions, int varIndex, int endIndex) {
        for (int i = endIndex; i >= 0; i--) {
            AbstractInsnNode node = instructions.get(i);
            if (node instanceof VarInsnNode) {
                VarInsnNode varInsnNode = (VarInsnNode) node;
                if (varInsnNode.var == varIndex) {
                    continue;
                } else {
                    return "var" + varInsnNode.var;
                }
            } else if (node instanceof FieldInsnNode) {
                FieldInsnNode fieldInsnNode = (FieldInsnNode) node;
                return fieldInsnNode.owner + "." + fieldInsnNode.name;
            }
        }
        return null;
    }

    private static String getParamSource(List<AbstractInsnNode> instructions, FieldInsnNode fieldInsnNode, int endIndex) {
        for (int i = endIndex; i >= 0; i--) {
            AbstractInsnNode node = instructions.get(i);
            if (node instanceof FieldInsnNode) {
                FieldInsnNode currentFieldInsnNode = (FieldInsnNode) node;
                if (currentFieldInsnNode.owner.equals(fieldInsnNode.owner) && currentFieldInsnNode.name.equals(fieldInsnNode.name)) {
                    continue;
                } else {
                    return currentFieldInsnNode.owner + "." + currentFieldInsnNode.name;
                }
            } else if (node instanceof VarInsnNode) {
                VarInsnNode varInsnNode = (VarInsnNode) node;
                return "var" + varInsnNode.var;
            }
        }
        return null;
    }

}
