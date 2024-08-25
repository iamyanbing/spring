package com.iamyanbing.exception;

import lombok.Data;

/**
 * 业务异常基类
 * <p>
 * （和客户相关）
 **/
@Data
public class CustomException extends RuntimeException {

    /**
     * 异常代码
     */
    private int code;

    /**
     * 本地化异常信息
     */
    private String localizedMessage;

    public CustomException() {
    }

    public CustomException(int code, String message) {
        super(message);
        this.code = code;
        this.localizedMessage = message;
    }

    public CustomException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.localizedMessage = message;
    }

}
