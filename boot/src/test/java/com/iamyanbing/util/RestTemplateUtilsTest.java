package com.iamyanbing.util;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @author huangyanbing
 * @date 2019-09-26 20:47
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MockServletContext.class)
@WebAppConfiguration
public class RestTemplateUtilsTest {


    private RestTemplate restTemplate = new RestTemplate();

    @Test
//    @Ignore
    public void testUserController() throws Exception {
        // 封装参数，千万不要替换为Map与HashMap，否则参数无法传递
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("name", "yanbing");
        HttpEntity<String> entity = new HttpEntity<>(params);

        //结果转化为ResponseEntity<String>，ResponseEntity除包含响应体之外还有状态行、响应头
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://127.0.0.1:9999/restTemplate/socketTimeout", entity, String.class);
        HttpHeaders httpHeaders = responseEntity.getHeaders();

    }

}
