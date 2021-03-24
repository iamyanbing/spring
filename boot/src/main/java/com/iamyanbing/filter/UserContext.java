package com.iamyanbing.filter;

import com.iamyanbing.domain.entity.User;

/**
 * @author pengcheng
 * @date 2020/4/23 23:15
 * @description
 */
public class UserContext {

    private static ThreadLocal<User> threadLocal = new ThreadLocal<>();

    public static void setUser(User userInfo) {
        threadLocal.set(userInfo);
    }

    public static User getUser() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }

}
