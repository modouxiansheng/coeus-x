package com.modou.coeus.parse.exception;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-08-09 11:39
 **/
public class CodeException extends RuntimeException{


    private CodeExceptionEnum exceptionCode;

    private String msg;

    public CodeException(CodeExceptionEnum exceptionCode) {
        this.exceptionCode = exceptionCode;
    }


    public CodeException(CodeExceptionEnum exceptionCode1, String message1) {
        String test = "ceshi";
        this.exceptionCode = exceptionCode1;
        this.msg = message1 + test;
    }

    public CodeException(Integer age , String message1,CodeExceptionEnum exceptionCode1) {
        String test = "ceshi";
        this.exceptionCode = exceptionCode1;
        this.msg = message1 + age;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
