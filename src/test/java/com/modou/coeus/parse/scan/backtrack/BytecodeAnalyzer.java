package com.modou.coeus.parse.scan.backtrack;



import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;
import jdk.internal.org.objectweb.asm.tree.analysis.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class BytecodeAnalyzer {



    public static void invoke(ClassNode classNode) throws AnalyzerException {
        for (MethodNode method : classNode.methods) {
            System.out.println(method.name + method.desc);

            Map<Integer, Object> localVarMap = new HashMap<>();
            Map<Integer, Object> argMap = new HashMap<>();

            InsnList instructions = method.instructions;

            Analyzer<BasicValue> analyzer = new Analyzer<>(new BasicInterpreter());
            Frame<BasicValue>[] frames = analyzer.analyze(classNode.name, method);

            for (int i = 0; i < instructions.size(); i++) {
                AbstractInsnNode insn = instructions.get(i);

                if (insn.getType() == AbstractInsnNode.VAR_INSN) {
                    VarInsnNode varInsn = (VarInsnNode) insn;
                    int varIndex = varInsn.var;

                    if (varInsn.getOpcode() == Opcodes.ALOAD || varInsn.getOpcode() == Opcodes.ILOAD || varInsn.getOpcode() == Opcodes.LLOAD
                            || varInsn.getOpcode() == Opcodes.FLOAD || varInsn.getOpcode() == Opcodes.DLOAD) {
                        if (argMap.containsKey(varIndex)) {
                            System.out.printf("Parameter %d is set to %s%n", varIndex, argMap.get(varIndex));
                        } else {
                            System.out.printf("Local variable %d is set to %s%n", varIndex, localVarMap.get(varIndex));
                        }
                    }
                } else if (insn.getType() == AbstractInsnNode.LDC_INSN) {
                    LdcInsnNode ldcInsn = (LdcInsnNode) insn;
                    if (argMap.containsValue(ldcInsn.cst)) {
                        int index = getKey(argMap, ldcInsn.cst);
                        System.out.printf("Parameter %d is set to constant %s%n", index, ldcInsn.cst);
                    } else {
                        localVarMap.put(getVarIndex(frames, i), ldcInsn.cst);
                    }
                } else if (insn.getType() == AbstractInsnNode.FIELD_INSN) {
                    FieldInsnNode fieldInsn = (FieldInsnNode) insn;
                    if (argMap.containsValue(fieldInsn.name)) {
                        int index = getKey(argMap, fieldInsn.name);
                        System.out.printf("Parameter %d is set to field %s.%s%n", index, fieldInsn.owner, fieldInsn.name);
                    } else {
                        localVarMap.put(getVarIndex(frames, i), fieldInsn.name);
                    }
                } else if (insn.getType() == AbstractInsnNode.METHOD_INSN) {
                    MethodInsnNode methodInsn = (MethodInsnNode) insn;
                    List<Object> args = new ArrayList<>();

                    for (int j = 0; j < methodInsn.desc.length(); j++) {
                        char c = methodInsn.desc.charAt(j);
                        if (c == 'L' || c == '[') {
                            args.add(localVarMap.get(getVarIndex(frames, i -j)));
                        } else if (c == 'I' || c == 'S' || c == 'B' || c == 'C' || c == 'Z') {
                            args.add(localVarMap.get(getVarIndex(frames, i - j)));
                        } else if (c == 'J') {
                            args.add(localVarMap.get(getVarIndex(frames, i - j)));
                            j++;
                        } else if (c == 'F') {
                            args.add(localVarMap.get(getVarIndex(frames, i - j)));
                        } else if (c == 'D') {
                            args.add(localVarMap.get(getVarIndex(frames, i - j)));
                            j++;
                        }
                    }

                    Collections.reverse(args);
                    System.out.printf("Method call: %s.%s(%s)%n", methodInsn.owner, methodInsn.name, args);

                    if (methodInsn.getOpcode() == Opcodes.INVOKESTATIC || methodInsn.getOpcode() == Opcodes.INVOKEDYNAMIC) {
                        for (int j = 0; j < methodInsn.desc.length(); j++) {
                            char c = methodInsn.desc.charAt(j);
                            if (c == 'L' || c == '[') {
                                localVarMap.remove(getVarIndex(frames, i - j));
                            } else if (c == 'J' || c == 'D') {
                                j++;
                            }
                        }
                    } else {
                        int varIndex = getVarIndex(frames, i - 1);
                        Object result = localVarMap.get(varIndex);
                        localVarMap.remove(varIndex);

                        if (argMap.containsValue(result)) {
                            int index = getKey(argMap, result);
                            System.out.printf("Parameter %d is set to the result of the method call%n", index);
                        } else {
                            System.out.printf("Local variable %d is set to the result of the method call%n", varIndex);
                            localVarMap.put(varIndex, result);
                        }
                    }
                } else if (insn.getType() == AbstractInsnNode.IINC_INSN) {
                    IincInsnNode iincInsn = (IincInsnNode) insn;
                    int varIndex = iincInsn.var;

                    if (argMap.containsKey(varIndex)) {
                        System.out.printf("Parameter %d is incremented by %d%n", varIndex, iincInsn.incr);
                    } else {
                        int oldValue = (int) localVarMap.getOrDefault(varIndex, 0);
                        int newValue = oldValue + iincInsn.incr;
                        System.out.printf("Local variable %d is incremented from %d to %d%n", varIndex, oldValue, newValue);
                        localVarMap.put(varIndex, newValue);
                    }
                } else if (insn.getType() == AbstractInsnNode.TYPE_INSN) {
                    TypeInsnNode typeInsn = (TypeInsnNode) insn;
                    if (typeInsn.getOpcode() == Opcodes.NEW) {
                        localVarMap.put(getVarIndex(frames, i), typeInsn.desc);
                    }
                } else if (insn.getType() == AbstractInsnNode.JUMP_INSN) {
                    JumpInsnNode jumpInsn = (JumpInsnNode) insn;
                    if (jumpInsn.getOpcode() == Opcodes.GOTO) {
                        System.out.println("Unconditional jump to " + jumpInsn.label);
                    }
                }
            }
        }
    }


    private static int getVarIndex(Frame<BasicValue>[] frames, int insnIndex) {
        return frames[insnIndex].getLocals() - 1;
    }

    private static <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        throw new IllegalArgumentException("No key found for value: " + value);
    }
}

