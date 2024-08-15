package com.iamyanbing.service;


import com.iamyanbing.res.ResponseResult;
import com.iamyanbing.entity.SysUser;

public interface LoginService {

    ResponseResult login(SysUser sysUser);

    ResponseResult logout();

    String login(String userName, String password, String code, String uuid);
}
