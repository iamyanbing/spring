package com.iamyanbing.controller;

import com.iamyanbing.common.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Ajax长轮询实现类
 */
@Controller
public class AjaxLongPollingController {

    private ExecutorService executorService = Executors.newFixedThreadPool(1);

    private static Logger logger = LoggerFactory.getLogger(AjaxShortPollingController.class);

    @RequestMapping("/longPolling")
    public String news(){
        // return 返回值中的 AjaxLongPolling 表示 AjaxLongPolling.jsp页面。
        return "AjaxLongPolling";
    }

    @RequestMapping(value="/ajaxLongPolling", produces="text/html;charset=UTF-8")
    @ResponseBody
    public DeferredResult<String> ajaxLongPolling(HttpServletRequest request){
        logger.info("ajaxLongPolling success");
        final DeferredResult<String> dr = new DeferredResult<String>();

        // 通过线程池异步执行任务
        // 线程执行任务完成，把结果setResult到DeferredResult
        // 异步执行的任务，可能是非常耗时的操作，比如请求三方获取数据等。
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    //hold主3秒
                    //前端不断发起请求，没有设置轮训间隔时间
                    //所以服务端要控制轮训间隔时间
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int index = new Random().nextInt(Const.NEWS.length);
                dr.setResult(Const.NEWS[index]);
            }
        });
        return dr;
    }

    /**
     * 用Callable 和 用DeferredResult 效果一样
     * 都是异步处理
     * @param request
     * @return
     */
    public Callable<String> ajaxLongPollin2(HttpServletRequest request){

        Callable callable = new Callable() {
            @Override
            public Object call() throws Exception {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int index = new Random().nextInt(Const.NEWS.length);
                return Const.NEWS[index];
            }
        };
        return callable;
    }

}
