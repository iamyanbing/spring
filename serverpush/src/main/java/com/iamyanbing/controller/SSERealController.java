package com.iamyanbing.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 类说明： 真正的SSE
 */
@Controller
public class SSERealController {
    private static Logger logger = LoggerFactory.getLogger(SSERealController.class);

    private static Map<String,SseEmitter> sseEmitters = new ConcurrentHashMap<>();
    private ExecutorService executorService = Executors.newFixedThreadPool(2);

    @RequestMapping("/sseReal")
    public String sseReal(){
        return "sseReal";
    }

    @RequestMapping(value="/payMoney")
    @ResponseBody
    public SseEmitter pay(String weCharId){
        //设置SseEmitter超时时间为80000L
        //不设置 sseEmitter.send("已通知自动售货机C9出货，请勿走开！") 会报错
        SseEmitter emitter = new SseEmitter(80000L);
        sseEmitters.put(weCharId,emitter);
        executorService.submit(new Pay(weCharId) );
        return emitter;
    }

    private static class Pay implements Runnable{

        private String weCharId;

        public Pay(String weCharId) {
            this.weCharId = weCharId;
        }

        @Override
        public void run() {
            SseEmitter sseEmitter = sseEmitters.get(weCharId);
            try {
                logger.info("联系支付服务，准备扣款");
                Thread.sleep(20000);
                //send之后，响应 支付完成 给前端
                sseEmitter.send("支付完成");
                logger.info("准备通知自动售货机");
                Thread.sleep(20000);
                //send之后，响应  已通知自动售货机C9出货，请勿走开！  给前端
                sseEmitter.send("已通知自动售货机C9出货，请勿走开！");
                //complete之后，连接断开，不是TCP长连接。
                sseEmitter.complete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
