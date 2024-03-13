package com.modou.coeus.utils;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2024-02-10 12:37
 **/
public class GraphCall {

    private final ModelGeneratorMethodVisitor.Handle callerMethod;
    private final ModelGeneratorMethodVisitor.Handle targetMethod;
    private final int callerArgIndex;
    private final String callerArgPath;
    private final int targetArgIndex;

    public GraphCall(ModelGeneratorMethodVisitor.Handle callerMethod, ModelGeneratorMethodVisitor.Handle targetMethod, int callerArgIndex, String callerArgPath, int targetArgIndex) {
        this.callerMethod = callerMethod;
        this.targetMethod = targetMethod;
        this.callerArgIndex = callerArgIndex;
        this.callerArgPath = callerArgPath;
        this.targetArgIndex = targetArgIndex;
    }

    public ModelGeneratorMethodVisitor.Handle getCallerMethod() {
        return callerMethod;
    }

    public ModelGeneratorMethodVisitor.Handle getTargetMethod() {
        return targetMethod;
    }

    public int getCallerArgIndex() {
        return callerArgIndex;
    }

    public String getCallerArgPath() {
        return callerArgPath;
    }

    public int getTargetArgIndex() {
        return targetArgIndex;
    }
}
