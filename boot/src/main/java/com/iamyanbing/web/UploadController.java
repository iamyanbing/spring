package com.iamyanbing.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * 上传文件可以通过在方法中声明MultipartFile excelFile获取流，也可以通过在方法中声明HttpServletRequest request获取流
 *
 *
 * 在properties文件中设置文件上传大小限制如下：
 * spring.servlet.multipart.max-file-size=20MB  ： 设置单个文件的大小
 * spring.servlet.multipart.max-request-size=20MB ： 设置单次请求的文件的总大小
 * 如果是想要不限制文件上传的大小，那么就把两个值都设置为-1
 *
 *
 * 上传文件大小超过限制报如下错误：
 * o.a.t.u.h.f.FileUploadBase$FileSizeLimitExceededException: The field images exceeds its maximum permitted size of 1048576 bytes.
 *
 *
 * @author huangyanbing
 * @date 2019-09-09 19:33
 */
@RestController
public class UploadController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 模拟 ： 接收前端上传文件，再传递给其他后台系统
     * <p>
     * 本地图片上传请参考RestTemplateUtils类uploadImage方法
     *
     * @param images
     * @param request
     * @return
     */
    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    public String uploadImage(MultipartFile[] images, HttpServletRequest request) {
        String path = "http://127.0.0.1:8080/uploadImages";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("name", "yanbing");
        for (MultipartFile image :
                images) {
            //ByteArrayResource类需要实现getFileName()方法返回一个文件名称.有些公司上传接口没有文件名程序会执行异常
            ByteArrayResource contentsAsResource = null;
            try {
                contentsAsResource = new ByteArrayResource(image.getBytes()) {
                    @Override
                    public String getFilename() {
                        return image.getOriginalFilename();
                    }// 此处从multipartFile获取byte[],如果是上传本地文件可以使用io获取byte[]
                };
            } catch (IOException e) {
                LOGGER.error("MultipartFile类转ByteArrayResource类失败。", e);
                return "MultipartFile类转ByteArrayResource类失败；" + e.getMessage();
            }
            params.add("images", contentsAsResource);
        }
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(params, headers);
        //不设置请求头方法
//        String responseDTO = restTemplate.postForObject(path, params, String.class);

        String responseDTO = restTemplate.postForObject(path, entity, String.class);

        return "success";
    }

    /**
     * 接收上面uploadImage方法上传的文件
     *
     * @param excelFile
     * @param name
     * @return
     */
    @RequestMapping(value = "/uploadImages", method = RequestMethod.POST)
    public String uploadImages(@RequestParam("images") MultipartFile[] excelFile, @RequestParam String name) {

        LOGGER.info(name);

        return "Hello " + name;
    }

    /**
     * 接收上面uploadImage方法上传的文件
     *
     * @return
     */
    @RequestMapping(value = "/uploadImagesFunctionTwo", method = RequestMethod.POST)
    public String uploadImagesFunctionTwo(MultipartHttpServletRequest request) {

        LOGGER.info(request.getParameter("name"));
        List<MultipartFile> images = request.getFiles("images");

        return "Hello ";
    }

    /**
     * 接收上面uploadImage方法上传的文件
     *
     * @return
     */
    @RequestMapping(value = "/uploadImagesFunctionThree", method = RequestMethod.POST)
    public String uploadImagesFunctionThree(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        LOGGER.info(multipartRequest.getParameter("name"));
        List<MultipartFile> images = multipartRequest.getFiles("images");

        return "Hello ";
    }


    /**
     * 通过MultipartFile接收文件
     *
     * @param excelFile
     * @param name
     * @return
     */
    @RequestMapping(value = "/uploadExcelToMultipartFile", method = RequestMethod.POST)
    public String uploadExcelToMultipartFile(@RequestParam("excelFile") MultipartFile excelFile, @RequestParam String name) {
        try {
            InputStream inputStream = excelFile.getInputStream();
        } catch (IOException e) {
            LOGGER.error("Failed to upload file", e);
        }
        return "Hello " + name;
    }

    /**
     * 通过HttpServletRequest接受文件
     *
     * @param request
     * @param name
     * @return
     */
    @RequestMapping(value = "/uploadFileToHttpServletRequest", method = RequestMethod.POST)
    public String uploadFileToHttpServletRequest(HttpServletRequest request, @RequestParam String name) {
        try {
            InputStream inputStream = request.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder body = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
            LOGGER.info(body.toString());
        } catch (IOException e) {
            LOGGER.error("Failed to upload file", e);
        }
        return "Hello " + name;
    }
}
