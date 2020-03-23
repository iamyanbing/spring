package com.iamyanbing.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @author huangyanbing
 * @date 2019-09-26 20:47
 */
public class RestTemplateUtils {

    private final static String url = "www.baidu.com";
    @Autowired
    private RestTemplate restTemplate;

    public void postForObject() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //Cookie在请求头
        headers.set("Cookie", "JSESSIONID=9114409C5EAF785B8CE6024CEA8864A5");

        // 封装参数，千万不要替换为Map与HashMap，否则参数无法传递
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", "username");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        //直接转化为需要的对象（这里为String）
        String responses = restTemplate.postForObject(url, request, String.class);

    }

    public void postForEntity() {
        String requestData = "";//requestData为json、xml

        HttpEntity<String> entity = new HttpEntity<>(requestData);

        //结果转化为ResponseEntity<String>，ResponseEntity除包含响应体之外还有状态行、响应头
        //setErrorHandler作用是当响应状态码不为200时不抛出异常，通过ResponseEntity对象中响应状态码自定义处理
        restTemplate.setErrorHandler(new CustomErrorHandler());
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
        HttpHeaders httpHeaders = responseEntity.getHeaders();
    }

}
