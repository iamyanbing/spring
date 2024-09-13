package com.iamyanbing.enums;

import lombok.Getter;


public enum CommonStatusEnum {


    /**
     * 成功
     */
    SUCCESS(200, "success"),

    USER_TOKEN(401, "对不起,您的 token 非法"),

    /**
     * 失败
     */
    FAIL(500, "系统异常");
    @Getter
    private int code;
    @Getter
    private String value;

    CommonStatusEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }
}
