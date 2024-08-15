package com.iamyanbing.service.impl;

import com.iamyanbing.common.Constants;
import com.iamyanbing.entity.LoginUser;
import com.iamyanbing.entity.SysUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


/**
 * 共三个权限：system:user:list、system:user:list、system:menu:list
 * admin 用户，密码：admin，userId = 1000，角色是 admin，拥有所有权限
 * test 用户，密码：test，userId = 1001，角色是 test，拥有权限包括 system:role:list、system:menu:list
 * 普通用户，密码：123456，userId = 9999，角色是 common，拥有权限为 system:menu:list
 * 普通用户可以是任意登录账号
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    /**
     * 登录的时候会调用本方法
     * 见 LoginServiceImpl 类
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //查询用户信息
        SysUser sysUser = obtainSysUser(username);
        if (Objects.isNull(sysUser)) {
            throw new RuntimeException("用户名或密码错误");
        }

        //查询用户权限信息
        List<String> perms = obtainPerms(sysUser.getUserId());

        //查询用户角色信息
        List<String> roles = obtainRoles(sysUser.getUserId());

        //方法的返回值是UserDetails类型,需要返回自定义的实现类,并且将user信息通过构造方法传入
        return new LoginUser(sysUser, perms, roles);
    }

    /**
     * 查询用户角色信息
     * 实际数据从mysql获取
     *
     * @param userId
     * @return
     */
    private List<String> obtainRoles(Long userId) {
        List<String> roles = new ArrayList<>();
        if (Constants.ADMIN_USER_ID.equals(userId)) {
            roles.add("admin");
        } else if (Constants.TEST_USER_ID.equals(userId)) {
            roles.add("test");
        } else {
            roles.add("common");
        }

        return roles;
    }

    /**
     * 查询用户权限信息
     * 实际数据从mysql获取
     *
     * @param userId
     * @return
     */
    private List<String> obtainPerms(Long userId) {
        List<String> perms;
        if (Constants.ADMIN_USER_ID.equals(userId)) {
            perms = new ArrayList<>(Arrays.asList("system:user:list", "system:role:list", "system:menu:list"));
        } else if (Constants.TEST_USER_ID.equals(userId)) {
            perms = new ArrayList<>(Arrays.asList("system:role:list", "system:menu:list"));
        } else {
            perms = new ArrayList<>(Arrays.asList("system:menu:list"));
        }

        return perms;
    }

    /**
     * 根据 用户名 查询用户信息
     * 实际数据从mysql获取
     *
     * @param username
     * @return
     */
    private SysUser obtainSysUser(String username) {
        SysUser sysUser = new SysUser();
        if (StringUtils.equals(username, Constants.ADMIN_USER_NAME)) {
            sysUser.setUserId(Constants.ADMIN_USER_ID);
            sysUser.setUserName(username);
            sysUser.setNickName("超级权限用户");
            sysUser.setPassword("$2a$10$wgIvBFsrp5i0RjN2BKKSeOXeSItoPN8.VgHrzQVnNGjEeofa7Trq6");
        } else if (StringUtils.equals(username, Constants.TEST_USER_NAME)) {
            sysUser.setUserId(Constants.TEST_USER_ID);
            sysUser.setUserName(username);
            sysUser.setNickName("测试用户");
            sysUser.setPassword("$2a$10$zsx2Kaa3qjKFZwrep3JT4exmKJib9BzesGE2JFVod3coodo2cTgVC");
        } else {
            sysUser.setUserId(Constants.COMMON_USER_ID);
            sysUser.setUserName(username);
            sysUser.setNickName("普通用户");
            sysUser.setPassword("$2a$10$qmUAfwgV5Q8Xo/L1EIkSWOVodJ3ukElbS2/GFkrgpQZJpzV8Xo04u");
        }

        // 明文密码使用方式，前面加 {noop}
        // sysUser.setPassword("{noop}123456");
        return sysUser;
    }
}
