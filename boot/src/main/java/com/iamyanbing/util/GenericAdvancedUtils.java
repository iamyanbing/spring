package com.iamyanbing.util;

import com.iamyanbing.domain.entity.BaseRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class GenericAdvancedUtils {
    @Autowired
    private RestTemplate restTemplate;
    private <T extends BaseRes> ResponseEntity<T> execute(String url, HttpEntity<String> entity, Class<T> clazz){
        //"asdasdasdasdas"填充请求url后带的参数；有多个uriVariables参数则按照顺序一个一个填充到url中。
        // 见RestTemplateUtils类getForEntity()
        ResponseEntity<T> responseEntity = restTemplate.postForEntity(url, entity, clazz, "asdasdasdasdas");
        T t = responseEntity.getBody();
        if (t.getCode() == 0){

        }
        return responseEntity;
    }
}
