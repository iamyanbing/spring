package com.createivearts.adminmanager.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author huangyanbing
 * @date 2019-10-14 16:23
 */
public class MyRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        Object o = DBContextHolder.get();
        if (o != null) {
            DBContextHolder.remmove();
        }
        return o;
    }
}
