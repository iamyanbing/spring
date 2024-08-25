package com.iamyanbing.service.impl;

import com.iamyanbing.enums.CommonStatusEnum;
import com.iamyanbing.entity.SysUser;
import com.iamyanbing.entity.LoginUser;
import com.iamyanbing.exception.AuthException;
import com.iamyanbing.req.LoginBody;
import com.iamyanbing.res.ResponseResult;
import com.iamyanbing.service.LoginService;
import com.iamyanbing.util.JwtUtil;
import com.iamyanbing.util.LoginUserContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author spikeCong
 * @date 2023/4/25
 **/
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    /**
     * 在 SecurityConfig 配置中将 AuthenticationManager 注入到IOC
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 登录
     *
     * @param sysUser
     * @return
     */
    @Override
    public ResponseResult login(SysUser sysUser) {

        //1.使用 AuthenticationManager 的 authenticate 方法进行用户认证
        //1.1 需要传入一个 Authentication 对象的实现,该对象包含用户信息
        // authenticationToken 就是我们创建出来的未认证的 Authentication 对象
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(sysUser.getUserName(), sysUser.getPassword());

        //2.如果认证没有通过,给出错误提示
        // 认证由 authenticationManager.authenticate() 帮我们完成。
        // 对未认证的 Authentication 和 UserDetails 进行匹配。
        // UserDetails 通过 UserDetailsServiceImpl 类获取,
        // 所以这里会调用 UserDetailsServiceImpl 类 loadUserByUsername 方法
        // authenticate 方法：将一个未认证的 Authentication 对象传入，返回一个认证完成的 Authentication
        // 如果认证失败，直接抛异常：AuthenticationException
        // AuthenticationEntryPointImpl 会捕获 AuthenticationException
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (AuthenticationException exception) {
            log.error("登录失败", exception);
            // 由全局异常捕获
            throw new AuthException(CommonStatusEnum.USERNAME_PASSWORD.getCode(),
                    CommonStatusEnum.USERNAME_PASSWORD.getMessage());
        }

        //3.如果认证通过,使用 userId 生成一个 JWT,并将其保存到 ResponseResult 对象中返回
        //3.1 获取经过身份验证的用户主体信息
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        //3.2 获取 userId、userName 生成JWT
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", loginUser.getSysUser().getUserId().toString());
        claims.put("userName", loginUser.getSysUser().getUserName());
        String jwt = JwtUtil.createJWT(claims, null);

        //4. 将用户信息缓存到Redis中,在下一次请求时能够识别出用户,userId作为key
        // redisCache.setCacheObject("login:" + userId, loginUser);
        //本项目用户信息就缓存在本地内存
        LoginUserContextUtil.setLoginUser(loginUser.getSysUser().getUserId(), loginUser);

        //5. 封装ResponseResult,并返回
        HashMap<String, String> map = new HashMap<>();
        map.put("token", jwt);

        return ResponseResult.success(map);
    }

    /**
     * 用户退出登录（登出、注销）
     * 一般就是清除 上下文对象和缓存 就行了
     *
     * @return
     */
    @Override
    public ResponseResult logout() {
        //用户信息（UsernamePasswordAuthenticationToken对象）在 JwtAuthenticationTokenFilter类 第五步添加 至 SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        if (Objects.isNull(authenticationToken)) {
            // 交给 AuthenticationEntryPointImpl 类处理
            throw new RuntimeException("获取用户认证信息失败!");
        }

        LoginUser loginUser = (LoginUser) authenticationToken.getPrincipal();
        Long userId = loginUser.getSysUser().getUserId();

        //1：清理缓存
        // redisCache.deleteObject("login:" + userId);
        //本项目直接删除 本地缓存中用户信息
        LoginUserContextUtil.clearLoginUser(userId);

        //2：清理上下文对象，释放内存空间
        SecurityContextHolder.clearContext();
        return ResponseResult.success();
    }

    /**
     * 带验证码的登录
     */
    @Override
    public ResponseResult loginCode(LoginBody loginBody) {

        //1.redis中获取验证码，完成验证码的验证
        // 记得删除缓存
        if (StringUtils.isBlank(loginBody.getCode())){
            throw new AuthException(CommonStatusEnum.CAPTCHA.getCode(),
                    CommonStatusEnum.CAPTCHA.getMessage());
        }

        //2.如果认证没有通过,给出错误提示
        //对authentication和UserDetails进行匹配
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginBody.getUserName(), loginBody.getPassword()));
        } catch (AuthenticationException exception) {
            log.error("登录失败", exception);
            throw new AuthException(CommonStatusEnum.USERNAME_PASSWORD.getCode(),
                    CommonStatusEnum.USERNAME_PASSWORD.getMessage());
        }

        //3.如果认证通过,使用userId生成一个JWT,并将其保存到ResponseResult中 返回
        //3.1 获取经过身份验证的用户主体信息
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        //3.2 获取 userId、userName 生成JWT
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", loginUser.getSysUser().getUserId().toString());
        claims.put("userName", loginUser.getSysUser().getUserName());
        String jwt = JwtUtil.createJWT(claims, null);

        //4. 将用户信息缓存到Redis中,在下一次请求时能够识别出用户,userId作为key
        // redisCache.setCacheObject("login:" + userId, loginUser);
        //本项目用户信息就缓存在本地内存
        LoginUserContextUtil.setLoginUser(loginUser.getSysUser().getUserId(), loginUser);

        //5. 封装ResponseResult,并返回
        HashMap<String, String> map = new HashMap<>();
        map.put("token", jwt);
        return ResponseResult.success(map);
    }
}
