package com.iamyanbing.controller;

import com.iamyanbing.req.LockRequest;
import com.iamyanbing.res.ResponseResult;
import com.iamyanbing.service.LockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 锁（redis、zookeeper）
 * </p>
 */
@RestController
@Slf4j
public class LockController {

    @Autowired
    @Qualifier("redisLock")
//    @Qualifier("redisLuaLock")
    // 默认用这个
//    @Qualifier("redissonBasicLock")
//    @Qualifier("redissonBasicYamlLock")
//    @Qualifier("redissonMasterSlaveYamlLock")
//    @Qualifier("redissonSentinelYamlLock")
//    @Qualifier("redissonClusterYamlLock")
//    @Qualifier("redissonRedLockLock")
//    @Qualifier("zookeeperLock")
//    @Qualifier("zookeeperCuratorLock")
    private LockService lockService;

    /**
     * @return
     */
    @PostMapping("/lock")
    public ResponseResult driverGrab(@RequestBody LockRequest req) {
        return lockService.execute(req);
    }
}