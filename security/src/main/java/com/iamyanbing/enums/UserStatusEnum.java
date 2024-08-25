package com.iamyanbing.enums;

import lombok.Getter;

/**
 * 用户状态
 * 停用之后还可以启动；
 * 但是注销之后就代表删除了该用户，永远不可以再使用
 * <p>
 * 注销必须是用户自己完成。
 * 停用这不一定是用户自己完成，有可能是软件提供商完成。
 **/
public enum UserStatusEnum {

    OK(0, "正常"),
    DISABLE(1, "停用"),
    CANCEL(2, "注销");

    @Getter
    private final int code;
    @Getter
    private final String message;

    UserStatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
