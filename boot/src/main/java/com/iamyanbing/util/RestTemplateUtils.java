package com.iamyanbing.util;

import com.google.gson.Gson;
import com.iamyanbing.domain.entity.BaseRes;
import com.iamyanbing.domain.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author huangyanbing
 * @date 2019-09-26 20:47
 */
@Component
public class RestTemplateUtils {

    private final static String url = "www.baidu.com";

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 本地图片上传
     *
     * 接收前端上传文件，再传递给其他后台系统，请参考UploadController类uploadImage方法
     * @param path1
     * @param path2
     */
    public void uploadImage(String path1, String path2) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("multipart/form-data");
        headers.setContentType(type);
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

//        FileSystemResource resource = new FileSystemResource(new File("C:\\Users\\Administrator\\Desktop\\40.txt"));
        FileSystemResource resource1 = new FileSystemResource(new File(path1));
        FileSystemResource resource2 = new FileSystemResource(new File(path2));
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("name", "yanbing");
        param.add("images", resource1);
        param.add("images", resource2);

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(param, headers);
        String responseDTO = restTemplate.postForObject(url, entity, String.class);
    }

    public void getForEntity() {
        String urlAll = url + "/login?account={account}&password={password}";
        Map<String, String> params = new HashMap<>();
        params.put("account", "huang");
        params.put("password", "123456");
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(urlAll, String.class, params);
        BaseRes getTokenRes = new Gson().fromJson(responseEntity.getBody(), BaseRes.class);
    }


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
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
        HttpHeaders httpHeaders = responseEntity.getHeaders();
    }

}
