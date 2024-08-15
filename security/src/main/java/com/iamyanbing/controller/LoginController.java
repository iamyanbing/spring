package com.iamyanbing.controller;

import com.iamyanbing.req.LoginBody;
import com.iamyanbing.res.ResponseResult;
import com.iamyanbing.entity.SysUser;
import com.iamyanbing.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 用户登录
     *
     * @return
     */
//    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody SysUser sysUser) {
        //登录
        return loginService.login(sysUser);
    }


    /**
     * 用户登录 + 验证码
     *
     * @param loginBody
     * @return
     */
    @PostMapping("/user/loginCode")
    public ResponseResult loginCode(@RequestBody LoginBody loginBody) {
        return loginService.loginCode(loginBody);
    }


    /**
     * 用户退出登录（登出、注销）
     *
     * @return
     */
    @GetMapping("/user/logout")
    public ResponseResult logout() {
        return loginService.logout();
    }
}
