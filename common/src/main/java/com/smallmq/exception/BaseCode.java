package com.smallmq.exception;

public enum BaseCode {
    VALID_EXCEPTION(10001, "参数校验异常"),
    UNKNOWN_EXCEPTION(10002, "未知异常");


    private int code;
    private String msg;

    BaseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
