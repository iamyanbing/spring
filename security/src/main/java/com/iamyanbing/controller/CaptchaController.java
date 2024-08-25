package com.iamyanbing.controller;

import com.iamyanbing.res.ResponseResult;
import com.iamyanbing.util.JwtUtil;
import com.wf.captcha.SpecCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * 前后端分离项目使用
 */
@RestController
@Slf4j
public class CaptchaController {

    /**
     * 获取验证码
     */
    @GetMapping("/captchaImage")
    public ResponseResult getCode() {
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 4);

        //生成验证码 以及验证码唯一标识
        String uuid = JwtUtil.getUUID();

        String code = specCaptcha.text().toLowerCase();
        log.info("生成的验证码：{}", code);

        //创建map
        HashMap<String, Object> map = new HashMap<>();
        map.put("uuid", uuid);
        map.put("img", specCaptcha.toBase64());

        return ResponseResult.success(map);
    }

}
