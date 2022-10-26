package com.smallmq.constant;

public enum ProductConstant {
    AttrEnum;
    public enum AttrEnum{
        ATTR_TYPE_BASE(1,"base"),ATTR_TYPE_SALE(0,"sale");
        private int code;
        private String msg;
        AttrEnum(int code, String msg) {
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

    public enum StatusEnum{
        NEW_SPU(0,"new"),SPU_UP(1,"up"),SPU_DOWN(2,"down");
        private int code;
        private String msg;
        StatusEnum(int code, String msg) {
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

}
