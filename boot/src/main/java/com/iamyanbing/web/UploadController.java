package com.iamyanbing.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 上传文件可以通过在方法中声明MultipartFile excelFile获取流，也可以通过在方法中声明HttpServletRequest request获取流
 *
 * @author huangyanbing
 * @date 2019-09-09 19:33
 */
@RestController
public class UploadController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);

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
