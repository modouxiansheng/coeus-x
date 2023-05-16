package com.modou.coeus.domain;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-05-16 16:56
 **/
public class ClassAndMethodData {

    /**
     * 类名
     */
    private String className;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 参数签名
     */
    private String paramSign;

    public ClassAndMethodData(String className, String methodName, String paramSign) {
        this.className = className;
        this.methodName = methodName;
        this.paramSign = paramSign;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getParamSign() {
        return paramSign;
    }
}
