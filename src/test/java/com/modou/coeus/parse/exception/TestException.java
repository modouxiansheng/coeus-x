package com.modou.coeus.parse.exception;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-08-13 16:32
 **/
public class TestException {

    public static void main(String[] args) {
        CodeException2 codeException2 = new CodeException2(CodeExceptionEnum.OPERATE_FAILED);
        if (codeException2 instanceof CodeException){
            System.out.println("CodeException");
        }
        if (codeException2 instanceof CodeException2){
            System.out.println("codeException2");
        }
    }
}
