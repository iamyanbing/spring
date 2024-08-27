package com.iamyanbing.enums;

import lombok.Getter;

public enum CommonStatusEnum {
    /**
     * message ： 操作成功
     */
    SUCCESS(200, "success"),

    CAPTCHA(4000, "验证码异常"),
    USERNAME_PASSWORD(4001, "用户不存在或者密码错误"),
    USER_DISABLE(4002, "对不起,您的账号已被停用"),
    USER_CANCEL(4002, "对不起,您的账号已注销"),

    USER_TOKEN(4003, "对不起,您的 token 非法"),

    USER_LOGIN(4004, "对不起,您的登录已经失效"),


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
