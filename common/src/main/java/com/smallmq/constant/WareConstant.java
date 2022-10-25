package com.smallmq.constant;

public enum WareConstant {
    PURCHASE_STATUS_CREATE(0, "新建"),
    PURCHASE_STATUS_ASSIGNED(1, "已分配"),
    // 正在采购
    PURCHASE_STATUS_PURCHASING(2, "正在采购"),
    // 已完成
    PURCHASE_STATUS_DONE(3, "已完成"),
    // 采购失败
    PURCHASE_STATUS_ERROR(4, "采购失败");

    private Integer code;
    private String msg;

    WareConstant(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }



}
