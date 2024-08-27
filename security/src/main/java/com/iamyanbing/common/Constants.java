package com.iamyanbing.common;

public class Constants {

    /**
     *
     */
    public static final String TOKEN = "Authorization";

    /**
     * admin 用户 名称
     */
    public static final String ADMIN_USER_NAME = "admin";
    /**
     * test 用户 名称
     */
    public static final String TEST_USER_NAME = "test";

    /**
     * admin 用户 id
     */
    public static final Long ADMIN_USER_ID = 1000L;

    /**
     * test 用户 id
     */
    public static final Long TEST_USER_ID = 1001L;

    /**
     * test 用户 id
     */
    public static final Long COMMON_USER_ID = 9999L;

    /**
     * 菜单类型（目录）
     */
    public static final String TYPE_DIR = "M";

    /**
     * 菜单类型（菜单）
     */
    public static final String TYPE_MENU = "C";

    /**
     * 菜单类型（按钮）
     */
    public static final String TYPE_BUTTON = "F";

    /**
     * Layout 组件标识
     */
    public final static String LAYOUT = "Layout";

    /**
     * ParentView 组件标识
     */
    public final static String PARENT_VIEW = "ParentView";

    /**
     * 认证异常路径
     * 作用：Filter 抛出的 AuthException，请求转发到 ExceptionController
     */
    public final static String AUTH_EXCRPTION_PATH = "/authException";



}
