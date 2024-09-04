package com.iamyanbing.req;

import lombok.Data;

@Data
public class LockRequest {
    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 司机ID
     */
    private Long driverId;

}
