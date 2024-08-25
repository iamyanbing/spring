package com.iamyanbing.controller;

import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;


/**
 * 前后端不分离项目使用
 */
@Controller
public class KaptchaController {

    private final Producer producer;

    /**
     * KaptchaConfig 生成 producer 对象
     *
     * @param producer
     */
    @Autowired
    public KaptchaController(Producer producer) {
        this.producer = producer;
    }

    @GetMapping("/code/image")
    public void getCode(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //1.创建验证码文本
        String textCode = producer.createText();

        //2.将验证码保存到session

        //3.将验证码返回,禁止验证码缓存
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        //2.设置 ContentType,响应验证码
        response.setContentType("image/png");
        BufferedImage bufferedImage = producer.createImage(textCode);
        ImageIO.write(bufferedImage, "jpg", response.getOutputStream());
    }

}
