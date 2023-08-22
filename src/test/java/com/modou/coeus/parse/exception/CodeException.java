package com.modou.coeus.parse.exception;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-08-09 11:39
 **/
public class CodeException extends RuntimeException{


    private CodeExceptionEnum exceptionCode;

    public CodeException(CodeExceptionEnum exceptionCode) {
        this.exceptionCode = exceptionCode;
    }


    public CodeException(CodeExceptionEnum exceptionCode, String message) {
        this.exceptionCode = exceptionCode;
    }
}
