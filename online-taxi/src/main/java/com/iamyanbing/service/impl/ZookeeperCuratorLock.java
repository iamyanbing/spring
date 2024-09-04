package com.iamyanbing.service.impl;

import com.iamyanbing.enums.CommonStatusEnum;
import com.iamyanbing.req.LockRequest;
import com.iamyanbing.res.ResponseResult;
import com.iamyanbing.service.LockService;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service("zookeeperCuratorLock")
@Slf4j
public class ZookeeperCuratorLock implements LockService {

    @Autowired
    CuratorFramework curatorFramework;

    @Override
    public ResponseResult execute(LockRequest req) {
        ResponseResult grab = null;
        // 创建 持久节点
        Long orderId = req.getOrderId();
        String parentNode = "/order-" + orderId;

        InterProcessMutex lock = new InterProcessMutex(curatorFramework, parentNode);

        try {
            if (lock.acquire(10, TimeUnit.SECONDS)) {
                log.info("获取锁成功");

                //TODO 执行业务代码

                log.info("任务执行完成");
                grab = ResponseResult.success();
            } else {
                grab = ResponseResult.fail(CommonStatusEnum.FAIL.getCode(), CommonStatusEnum.FAIL.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 只会释放自己的锁，不会释放其他线程的锁。
                lock.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return grab;
    }
}
