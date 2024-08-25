package com.iamyanbing.exception;

/**
 * 认证异常
 * <p>
 * 与 AuthenticationEntryPointImpl 类区别？
 * 这里放回给客户端的异常信息更准确。
 * eg：验证码错误、用户名或者密码错误 等
 * <p>
 * AuthException 通过全局异常 GlobalExceptionHandler 类捕获
 **/
public class AuthException extends CustomException {

    public AuthException(int code, String message) {
        super(code, message);
    }
}
