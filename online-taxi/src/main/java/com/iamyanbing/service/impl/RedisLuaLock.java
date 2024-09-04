package com.iamyanbing.service.impl;

import com.iamyanbing.enums.CommonStatusEnum;
import com.iamyanbing.req.LockRequest;
import com.iamyanbing.res.ResponseResult;
import com.iamyanbing.service.LockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service("redisLuaLock")
@Slf4j
public class RedisLuaLock implements LockService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RenewRedisLock renewRedisLock;

    @Autowired
    @Qualifier("redisSetScript")
    DefaultRedisScript<Boolean> redisSetScript;

    @Autowired
    @Qualifier("redisDelScript")
    DefaultRedisScript<Boolean> redisDelScript;

    @Override
    public ResponseResult execute(LockRequest req) {
        String orderId = req.getOrderId() + "";
        String driverId = req.getDriverId() + "";
        String key = orderId;
        String value = driverId + "-" + UUID.randomUUID();
        // 设置加锁的key,设置过期时间，避免死锁 -lua

        List<String> strings = Arrays.asList(key, value);
        Boolean aBoolean = stringRedisTemplate.execute(redisSetScript, strings, "30");

        if (aBoolean) {
            //锁续期
            renewRedisLock.renewRedisLock(key, value, 20);
            log.info("获取锁成功");

            //TODO 执行业务代码

            log.info("任务执行完成");

            List<String> keyArg = Arrays.asList(key);
            Boolean dBoolean = stringRedisTemplate.execute(redisDelScript, keyArg, value);
            log.info("删除锁：" + dBoolean);
            return ResponseResult.success();
        } else {
            // 获取锁失败
            return ResponseResult.fail(CommonStatusEnum.FAIL.getCode(), CommonStatusEnum.FAIL.getMessage());
        }
    }
}