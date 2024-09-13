package com.iamyanbing.exception;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iamyanbing.enums.CommonStatusEnum;
import com.iamyanbing.res.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 用于网关的全局异常处理
 */
@Slf4j
public class MyExceptionHandler implements WebExceptionHandler {
    /**
     * 在这个方法中定义了遇到不同异常时候的处理办法
     *
     * @param exchange
     * @param ex
     * @return
     */
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.error("全局异常", ex);

        ResponseResult responseResult;
        // 在这里判断，不同异常，响应不同信息
        if (ex instanceof TokenExpiredException) {
            responseResult = ResponseResult.fail(CommonStatusEnum.USER_TOKEN.getCode(),
                    CommonStatusEnum.USER_TOKEN.getValue());
        } else {
            responseResult = ResponseResult.fail();
        }

        return writeResponse(exchange, responseResult);
    }

    private Mono<Void> writeResponse(ServerWebExchange exchange, ResponseResult responseResult) {

        ServerHttpResponse response = exchange.getResponse();
        // JOSN格式返回
//        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.getHeaders().add("Content-Type", "application/json;charset=utf-8");

        String resultString = "";
        try {
            resultString = new ObjectMapper().writeValueAsString(responseResult);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        DataBuffer wrap = response.bufferFactory().wrap(resultString.getBytes());

        return response.writeWith(Flux.just(wrap));

    }
}
