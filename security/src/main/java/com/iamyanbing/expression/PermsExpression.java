package com.iamyanbing.expression;

import com.iamyanbing.entity.LoginUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * 自定义权限校验规则
 **/
@Component("permsExpression")
public class PermsExpression {
    private static final String ALL_PERMISSION = "*:*:*";

    /**
     * 验证用户是否具备某一个权限
     *
     * @param permission
     * @return: boolean
     */
    public boolean hasPerms(String permission) {
        if (StringUtils.isBlank(permission)) {
            return false;
        }
        //获取当前用户的权限
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        if (Objects.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getPermissions())) {
            return false;
        }

        return hasPermissions(loginUser.getPermissions(), permission);
    }

    /**
     * 判断是否包含权限
     *
     * @param permissions 权限列表
     * @param permission  权限字符串
     * @return: boolean
     */
    private boolean hasPermissions(List<String> permissions, String permission) {
        return permissions.contains(permission) || permissions.contains(ALL_PERMISSION);
    }

    /**
     * 验证用户是否有任意一个权限
     *
     * @param permissions
     * @return: boolean
     */
    public boolean hasAnyPerms(String permissions) {
        if (StringUtils.isBlank(permissions)) {
            return false;
        }
        //获取当前用户的权限
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        if (Objects.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getPermissions())) {
            return false;
        }

        List<String> authorities = loginUser.getPermissions();
        for (String perms : permissions.split(",")) {
            if (perms != null && hasPermissions(authorities, perms)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断用户是否拥有某个角色
     *
     * @param role
     * @return: boolean
     */
    public boolean hasRole(String role) {
        if (StringUtils.isBlank(role)) {
            return false;
        }
        //获取当前用户的权限
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        if (Objects.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getRoles())) {
            return false;
        }
        for (String sysRole : loginUser.getRoles()) {
            if ("admin".equals(sysRole) || sysRole.equals(role)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断用户是否拥有任意一个角色
     *
     * @param roles
     * @return: boolean
     */
    public boolean hasAnyRole(String roles) {
        if (StringUtils.isBlank(roles)) {
            return false;
        }
        //获取当前用户的权限
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        if (Objects.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getRoles())) {
            return false;
        }
        for (String role : roles.split(",")) {
            if (hasRole(role)) {
                return true;
            }
        }
        return false;
    }
}
