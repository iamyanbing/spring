package com.iamyanbing.service.impl;

import com.iamyanbing.enums.CommonStatusEnum;
import com.iamyanbing.req.LockRequest;
import com.iamyanbing.res.ResponseResult;
import com.iamyanbing.service.LockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * redis 锁
 * 设置key
 * 设置过期时间
 */
@Service("redisLock")
@Slf4j
public class RedisLock implements LockService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RenewRedisLock renewRedisLock;

    @Override
    public ResponseResult execute(LockRequest req) {
        String orderId = req.getOrderId() + "";
        String driverId = req.getDriverId() + "";
        String key = orderId;
        String value = driverId + "-" + UUID.randomUUID();

        //两步操作，非原子操作。
        //当第一步执行成功，第二步还没有执行就宕机了，则会有问题
//        stringRedisTemplate.opsForValue().setIfAbsent(key, value);
//        stringRedisTemplate.expire(key, 20, TimeUnit.SECONDS);

        // 设置加锁的key,设置过期时间，避免死锁
        Boolean aBoolean = stringRedisTemplate.opsForValue().setIfAbsent(key, value, 20, TimeUnit.SECONDS);

        if (aBoolean) {
            //锁续期
            renewRedisLock.renewRedisLock(key, value, 20);
            log.info("获取锁成功");
            try {
                // 为了方便查看 redis 效果
                TimeUnit.SECONDS.sleep(40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //TODO 执行业务代码

            log.info("任务执行完成");

            String s = stringRedisTemplate.opsForValue().get(key);
            if (s.equals(value)) {
                // 只删除自己加的锁
                stringRedisTemplate.delete(key);
            }

            return ResponseResult.success();
        } else {
            // 获取锁失败
            return ResponseResult.fail(CommonStatusEnum.FAIL.getCode(), CommonStatusEnum.FAIL.getMessage());
        }
    }
}