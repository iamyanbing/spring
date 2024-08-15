package com.iamyanbing.req;


import lombok.Data;

/**
 * 用户登录对象 + 验证码
 */
@Data
public class LoginBody {
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String password;

    /**
     * 验证码
     */
    private String code;

    /**
     * 唯一标识
     */
    private String uuid = "";

}
