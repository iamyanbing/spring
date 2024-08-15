package com.iamyanbing.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class LoginUser implements UserDetails {

    private SysUser sysUser;

    public LoginUser() {
    }

    public LoginUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }

    //存储权限的集合
    private List<String> permissions;

    //存储角色信息的集合
    private List<String> roles;

    public LoginUser(SysUser sysUser, List<String> permissions) {
        this.sysUser = sysUser;
        this.permissions = permissions;
    }


    public LoginUser(SysUser sysUser, List<String> permissions, List<String> roles) {
        this.sysUser = sysUser;
        this.permissions = permissions;
        this.roles = roles;
    }

    public SysUser getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }

    //authorities 集合不需要序列化,只需要序列化 permissions 集合即可
    @JSONField(serialize = false)
    private List<SimpleGrantedAuthority> authorities;

    /**
     * 用于获取用户被授予的权限，
     * 可以用于实现访问控制。
     *
     * @param
     * @return: java.util.Collection<? extends org.springframework.security.core.GrantedAuthority>
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        //优化代码，将权限的集合提取到方法外，只有第一次调用才需要执行后面代码，再次调用则直接返回 authorities
        if (!ObjectUtils.isEmpty(authorities)) {
            return authorities;
        }

        if (authorities == null) {
            authorities = new ArrayList<>();
        }

        if (!ObjectUtils.isEmpty(permissions)) {
            //转换权限信息
            //1.8 语法完成
            authorities.addAll(
                    permissions.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList())
            );
        }

        if (!ObjectUtils.isEmpty(roles)) {
            //转换角色信息
            authorities.addAll(
                    roles.stream()
//                            .map(role -> new SimpleGrantedAuthority(role))//spring security 会自动增加 "ROLE_" 进行查找验证，所以这里会报错
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))   //admin 进行拼接 ROLE_admin
                            .collect(Collectors.toList())
            );
        }

        //authorities  ['ROLE_admin','system:user:list']
        return authorities;
    }

    /**
     * 用于获取用户的密码，
     * 一般用于进行密码验证。
     *
     * @param
     * @return: java.lang.String
     */
    @Override
    public String getPassword() {
        return sysUser.getPassword();
    }

    /**
     * 用于获取用户的用户名，
     * 一般用于进行身份验证。
     *
     * @param
     * @return: java.lang.String
     */
    @Override
    public String getUsername() {
        return sysUser.getUserName();
    }

    /**
     * 用于判断用户的账户是否未过期，
     * 可以用于实现账户有效期控制。
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 用于判断用户的账户是否未锁定，
     * 可以用于实现账户锁定功能。
     *
     * @param
     * @return: boolean
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 用于判断用户凭证（如密码）是否过期，
     * 可以用于实现密码有效期控制。
     *
     * @param
     * @return: boolean
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 用于判断用户是否已激活，
     * 可以用于实现账户激活功能。
     *
     * @param
     * @return: boolean
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
