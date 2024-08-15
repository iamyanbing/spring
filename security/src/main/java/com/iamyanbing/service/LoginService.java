package com.iamyanbing.service;


import com.iamyanbing.req.LoginBody;
import com.iamyanbing.res.ResponseResult;
import com.iamyanbing.entity.SysUser;

public interface LoginService {

    ResponseResult login(SysUser sysUser);

    ResponseResult logout();

    ResponseResult loginCode(LoginBody loginCode);
}
