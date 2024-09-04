package com.iamyanbing.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 自定义实现锁续期
 * <p>
 * 为什么要单独新建一个类，而不是把 renewRedisLock 方法放在 RedisLock 类？
 * 因为把 renewRedisLock 方法放在 RedisLock 类，相当于调用普通方法，
 * 只有 renewRedisLock 执行完，RedisLock 类 execute 方法中代码才会往下走。就算 renewRedisLock 方法上加 @Async，也是一样。
 * 想要走 spring 的动态代理（即想要实现异步），则必须要在这里新建一个类才可以。
 *
 * @Async 需要被 spring 容器管理？这句话什么意思？
 */
@Service
@Slf4j
public class RenewRedisLock {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 异步、递归
     * <p>
     * 实现锁需求
     *
     * @param key
     * @param value
     * @param timePeriod
     */
    @Async
    public void renewRedisLock(String key, String value, int timePeriod) {
        log.info("异步加锁");
        String s = stringRedisTemplate.opsForValue().get(key);
        if (StringUtils.isNotBlank(s) && s.equals(value)) {
            // 30s,当加好锁的时候，10s 续期一次。
            int renewPeriod = timePeriod / 3;

            try {
                TimeUnit.SECONDS.sleep(renewPeriod);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stringRedisTemplate.expire(key, timePeriod, TimeUnit.SECONDS);
        } else {
            return;
        }
        renewRedisLock(key, value, timePeriod);
    }
}
