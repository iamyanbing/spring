package com.iamyanbing.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUser {

    /**
     * 主键
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号
     */
    private String phonenumber;

    /**
     * 用户性别
     * 0：男
     * 1：女
     * 2：未知
     */
    private String sex;

    /**
     * 账号状态
     * 0：正常
     * 1：停用
     * 2：注销
     * <p>
     * 停用之后还可以启动；
     * 但是注销之后就代表删除了该用户，永远不可以再使用
     * <p>
     * 注销必须是用户自己完成。
     * 停用这不一定是用户自己完成，有可能是软件提供商完成。
     */
    private Integer status;
}
