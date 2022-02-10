package com.iamyanbing.snowflake;

public class UtilIdHub {

    private SnowflakeWorker worker;

    private UtilIdHub() {

    }

    public UtilIdHub(long workerId, long dataCenterId) {
        worker = new SnowflakeWorker(workerId, dataCenterId);
    }

    public long nextId() {
        return worker.nextId();
    }
}
