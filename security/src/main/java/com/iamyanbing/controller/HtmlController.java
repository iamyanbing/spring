package com.iamyanbing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 用来访问 templates 下 .html 文件
 *
 * 前后端不分离，不用
 */
@Controller
public class HtmlController {

    /**
     * 请求转发到 templates 下 login.html(登陆页面)
     *
     * @return
     */
    @RequestMapping("/login.html")
    public String login() {
        // 这里就会直接使用视图解析到 templates 下的 login.html
        return "login";
    }
}
