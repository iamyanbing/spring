package com.iamyanbing.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Random;

/**
 *类说明：实现sse推送
 */
@Controller
public class SSEController {

    private static Logger logger = LoggerFactory.getLogger(SSEController.class);

    @RequestMapping("/sse")
    public String stock(){
        return "sse";
    }

    //和Ajax长轮询比，最大区别就是 produces值不一样
    @RequestMapping(value="needPrice",produces = "text/event-stream;charset=UTF-8")
    @ResponseBody
    public String needPrice(){
        Random r = new Random();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //字符串拼接
        StringBuilder sb = new StringBuilder("");
        sb.append("retry:2000\n")
                .append("data:")
                .append((r.nextInt(1000)+50)+",")
                .append((r.nextInt(800)+100)+",")
                .append((r.nextInt(2000)+150)+",")
                .append((r.nextInt(1500)+100)+",")
                .append("\n\n");
        return sb.toString();
        //return之后，sse连接就断开了，不是TCP长连接。
        //sse支持断线重连，重连时间间隔由 retry 指定。
        //2000毫秒之后，客户端会重新连接服务器
        //sse：服务端控制客户端下次请求间隔时间（断线重连）
        //想用SSE，主要使用spring SseEmitter类， 见 SSERealController 类中代码
    }

}
