package com.modou.coeus.parse.exception;

import java.util.ArrayList;
import java.util.List;

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
        errorInfo.setMsg("xxx");
        test(errorInfo);
        test3();
        throw new CodeException(CodeExceptionEnum.OPERATE_FAILED);
    }

    public void test(ErrorInfo errorInfo){
        test2();
        throw new CodeException(1,errorInfo.getMsg(),CodeExceptionEnum.OPERATE_FAILED);
    }

    public void test2(){

        throw new CodeException(2,"测试",CodeExceptionEnum.OPERATE_FAILED);
    }

    public void test3(){
        List<CodeException> list = new ArrayList<>();
        if (list != null && !list.isEmpty()){
            list.get(0).setMsg("xxx");
        }
    }


    public static class ErrorInfo{
        private String msg;

        public String getMsg(){
            return this.msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
