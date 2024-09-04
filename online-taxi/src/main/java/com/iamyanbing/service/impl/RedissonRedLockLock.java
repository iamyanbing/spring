package com.iamyanbing.service.impl;

import com.iamyanbing.req.LockRequest;
import com.iamyanbing.res.ResponseResult;
import com.iamyanbing.service.LockService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonRedLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service("redissonRedLockLock")
@Slf4j
public class RedissonRedLockLock implements LockService {

    @Autowired
    @Qualifier("redissonClient1")
    RedissonClient redissonClient1;

    @Autowired
    @Qualifier("redissonClient2")
    RedissonClient redissonClient2;

    @Autowired
    @Qualifier("redissonClient3")
    RedissonClient redissonClient3;

    @Autowired
    @Qualifier("redissonClient4")
    RedissonClient redissonClient4;

    @Autowired
    @Qualifier("redissonClient5")
    RedissonClient redissonClient5;

    @Override
    public ResponseResult execute(LockRequest req) {

        String orderId = req.getOrderId() + "";

        String key = orderId;

        // 红锁
        RLock rLock1 = redissonClient1.getLock(key);
        RLock rLock2 = redissonClient2.getLock(key);
        RLock rLock3 = redissonClient3.getLock(key);
        RLock rlock4 = redissonClient4.getLock(key);
        RLock rlock5 = redissonClient5.getLock(key);

        RedissonRedLock lock = new RedissonRedLock(rLock1, rLock2, rLock3, rlock4, rlock5);
        lock.lock();

        log.info("获取锁成功");

        try {
            TimeUnit.SECONDS.sleep(40);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //TODO 执行业务代码

        log.info("任务执行完成");

        lock.unlock();

        return ResponseResult.success();
    }
}