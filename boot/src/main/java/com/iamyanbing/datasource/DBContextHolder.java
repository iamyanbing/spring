package com.iamyanbing.datasource;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author huangyanbing
 * @date 2019-10-14 16:20
 */
public class DBContextHolder {
    private static final ThreadLocal<DBTypeEnum> contextHolder = new ThreadLocal<>();

    private static final AtomicInteger counter = new AtomicInteger(-1);

    public static void set(DBTypeEnum dbType) {
        contextHolder.set(dbType);
    }

    public static DBTypeEnum get() {
        return contextHolder.get();
    }

    public static void remmove() {
        contextHolder.remove();
    }

    public static void master() {
        set(DBTypeEnum.MASTER);
    }

    public static void slave() {
        set(DBTypeEnum.SLAVE);
    }

    public static void dwdUser() {
        set(DBTypeEnum.DWDUSER);
    }
}
