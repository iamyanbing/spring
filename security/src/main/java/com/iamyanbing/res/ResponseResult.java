package com.iamyanbing.res;


import com.iamyanbing.enums.CommonStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ResponseResult<T> {

    //状态码
    private int code;

    //提示信息
    private String message;

    //封装结果数据
    private T data;

    /**
     * 成功响应的方法
     *
     * @param <T>
     * @return
     */
    public static <T> ResponseResult success() {
        return new ResponseResult()
                .setCode(CommonStatusEnum.SUCCESS.getCode())
                .setMessage(CommonStatusEnum.SUCCESS.getMessage());
    }

    /**
     * 成功响应的方法
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ResponseResult success(T data) {
        return new ResponseResult()
                .setCode(CommonStatusEnum.SUCCESS.getCode())
                .setMessage(CommonStatusEnum.SUCCESS.getMessage())
                .setData(data);
    }

    /**
     * 失败响应的方法
     * <p>
     * 没有指定code、message，默认返回系统异常
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ResponseResult fail(T data) {
        return new ResponseResult()
                .setCode(CommonStatusEnum.FAIL.getCode())
                .setMessage(CommonStatusEnum.FAIL.getMessage())
                .setData(data);
    }

    /**
     * 失败：自定义失败 错误码和提示信息
     *
     * @param code
     * @param message
     * @return
     */
    public static ResponseResult fail(int code, String message) {
        return new ResponseResult()
                .setCode(code)
                .setMessage(message);
    }

    /**
     * 失败：自定义失败 错误码、提示信息、具体错误
     *
     * @param code
     * @param message
     * @param data
     * @return
     */
    public static ResponseResult fail(int code, String message, String data) {
        return new ResponseResult()
                .setCode(code)
                .setMessage(message)
                .setData(data);
    }

}
