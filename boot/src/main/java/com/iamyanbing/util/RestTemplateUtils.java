package com.iamyanbing.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

        // 封装参数，千万不要替换为Map与HashMap，否则参数无法传递
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", "username");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        String responses = restTemplate.postForObject(url, request, String.class);
    }

}
