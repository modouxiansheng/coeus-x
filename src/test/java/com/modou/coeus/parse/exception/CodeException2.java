package com.modou.coeus.parse.exception;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-08-13 16:32
 **/
public class CodeException2 extends CodeException{


    public CodeException2(CodeExceptionEnum exceptionCode) {
        super(exceptionCode);
    }

    public CodeException2(CodeExceptionEnum exceptionCode, String message) {
        super(exceptionCode, message);
    }

}
