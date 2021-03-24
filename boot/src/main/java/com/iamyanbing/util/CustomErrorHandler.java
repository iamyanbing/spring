package com.iamyanbing.util;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import java.io.IOException;

/**
 * 自定义restTemplate错误处理类 屏蔽restTemplate原来的错误处理
 *
 * @author huangyanbing
 * @date 2019-09-30 10:51
 */
public class CustomErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return false;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {

    }
}
