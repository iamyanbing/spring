package com.iamyanbing.controller;

import com.iamyanbing.req.TransactionRequest;
import com.iamyanbing.res.ResponseResult;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    /**
     * 司机抢单
     *
     * @param req
     * @return
     */
    @PostMapping("/seata")
    @GlobalTransactional(rollbackFor = Exception.class)
    public ResponseResult grab(@RequestBody TransactionRequest req) {
        // TODO 调用 “订单服务”，修改订单信息。（订单生成由乘客完成）

        // TODO 调用 “工作状态服务”，修改司机工作状态

        return ResponseResult.success();
    }

}
