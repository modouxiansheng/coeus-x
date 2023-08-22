package com.modou.coeus.parse.exception;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-08-09 11:38
 **/
public class A {

    public void run(){
        B b = new B();
        b.run();
        ErrorInfo errorInfo = new ErrorInfo();
        test(errorInfo);
        throw new CodeException(CodeExceptionEnum.OPERATE_FAILED);
    }

    public void test(ErrorInfo errorInfo){

        throw new CodeException(CodeExceptionEnum.OPERATE_FAILED,errorInfo.getMsg());
    }


    public static class ErrorInfo{
        private String msg;

        public String getMsg(){
            return this.msg;
        }
    }
}
