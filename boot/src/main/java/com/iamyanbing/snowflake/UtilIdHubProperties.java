package com.iamyanbing.snowflake;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("utilhub")
public class UtilIdHubProperties {
    private long workerId;
    private long datacenterId;

    public long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(long workerId) {
        this.workerId = workerId;
    }

    public long getDatacenterId() {
        return datacenterId;
    }

    public void setDatacenterId(long datacenterId) {
        this.datacenterId = datacenterId;
    }
}
