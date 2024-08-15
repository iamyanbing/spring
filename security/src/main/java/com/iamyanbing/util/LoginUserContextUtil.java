package com.iamyanbing.util;

import com.iamyanbing.entity.LoginUser;
import org.springframework.util.ObjectUtils;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 登录成功之后把用户信息缓存到本地
 * 企业开发用Redis缓存
 */
public class LoginUserContextUtil {

    private static final ConcurrentHashMap<Long, LoginUser> CONTEXT = new ConcurrentHashMap();

    /**
     * 构造器私有化，禁止外部实例化
     */
    private LoginUserContextUtil() {
    }

    /**
     * 设置登录用户信息
     */
    public static void setLoginUser(Long userId, LoginUser loginUser) {
        if (userId == null || loginUser == null) {
            // 抛异常 : 401  认证失败
            return;
        }
        CONTEXT.put(userId, loginUser);
    }

    /**
     * 获取登录用户信息
     */
    public static LoginUser getLoginUser(Long userId) {
        LoginUser loginUser = CONTEXT.get(userId);
        if (ObjectUtils.isEmpty(loginUser)) {
            // 抛异常 : 401  认证失败
            return null;
        }
        return loginUser;
    }

    /**
     * 删除登录用户信息
     */
    public static void clearLoginUser(Long userId) {
        CONTEXT.remove(userId);
    }
}
