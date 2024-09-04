package com.iamyanbing.enums;

import lombok.Getter;

public enum CommonStatusEnum {
    /**
     * message ： 操作成功
     */
    SUCCESS(200, "success"),

    /**
     * message ： 操作失败
     */
    FAIL(500, "系统异常"),
    ;

    @Getter
    private int code;
    @Getter
    private String message;

    private CommonStatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getDescByCode(final int code) {
        String desc = null;
        for (CommonStatusEnum tmp : CommonStatusEnum.values()) {
            if (tmp.getCode() == code) {
                desc = tmp.getMessage();
                break;
            }
        }
        return desc;
    }

}
