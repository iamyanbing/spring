package com.iamyanbing.service.impl;

import com.iamyanbing.req.LockRequest;
import com.iamyanbing.res.ResponseResult;
import com.iamyanbing.service.LockService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("redissonBasicYamlLock")
@Slf4j
public class RedissonBasicYamlLock implements LockService {

    @Autowired
    @Qualifier("redissonYamlClient")
    RedissonClient redissonClient;

    @Override
    public ResponseResult execute(LockRequest req) {

        String orderId = req.getOrderId() + "";

        String key = orderId;

        RLock lock = redissonClient.getLock(key);
        lock.lock();
        // 可以通过 tryLock 方法判断是否获取到了锁。
        // lock.tryLock();

        log.info("获取锁成功");
//        try {
//            TimeUnit.SECONDS.sleep(40);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        //TODO 执行业务代码

        log.info("任务执行完成");
        lock.unlock();

        return ResponseResult.success();
    }
}
