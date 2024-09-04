package com.iamyanbing.service;


import com.iamyanbing.req.LockRequest;
import com.iamyanbing.res.ResponseResult;

public interface LockService {

    /**
     * @param req
     * @return
     */
    ResponseResult execute(LockRequest req);
}
