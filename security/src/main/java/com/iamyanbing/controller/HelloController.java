package com.iamyanbing.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    //    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/hello")
    public String hello() {

        return "hello";
    }

    /**
     * @return
     * @PreAuthorize() 在方法执行前进行权限校验
     * hasAuthority('system:user:list') 检查当前用户是否有 system:user:list 的权限
     * <p>
     * 在方法执行之后进行权限校验,可以使用 @PostAuthorize()
     */
    @GetMapping("/hasAuthority")
    @PreAuthorize("hasAuthority('system:user:list')")
    public String helloAdmin() {
        return "hasAuthority";
    }

    @RequestMapping("/hasAnyAuthority")
    @PreAuthorize("hasAnyAuthority('system:user:list','system:role:list','system:menu:list')")
    public String hasAnyAuthority() {
        return "hasAnyAuthority";
    }

    /**
     * 当前用户是 admin 角色,并且具有 system:role:list 或者 system:menu:list
     *
     * @return
     */
    @RequestMapping("/hasRole")
    @PreAuthorize("hasRole('admin') AND hasAnyAuthority('system:user:list','system:role:list')")
    public String hasRole() {
        return "hasRole";
    }

    /**
     * 当前用户拥有 admin 或者 test 角色,或者具有 system:role:list 权限
     * @return
     */
    @RequestMapping("/hasAnyRole")
    @PreAuthorize("hasAnyRole('admin','test') OR hasAuthority('system:role:list')")
    public String hasAnyRole() {
        return "hasAnyRole";
    }

}
