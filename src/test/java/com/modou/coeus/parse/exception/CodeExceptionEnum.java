package com.modou.coeus.parse.exception;

public enum CodeExceptionEnum {

    OPERATE_FAILED(111111, "操作失败", "操作失败，请稍后重试"),
    ;

    //异常响应CODE
    public long code;

    // 响应CODE对应的异常提示MESSAGE
    public String msg;

    // 前端交互展示的toast信息
    public String toast;

    CodeExceptionEnum(long code, String msg, String toast) {
        this.code = code;
        this.msg = msg;
        this.toast = toast;
    }

    public long getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getToast() {
        return toast;
    }
}
