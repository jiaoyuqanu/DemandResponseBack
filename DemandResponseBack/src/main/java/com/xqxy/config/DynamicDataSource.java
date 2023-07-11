package com.xqxy.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @Description
 * @ClassName DynamicDataSource
 * @Author dongdawei
 * @date 2021.04.13 17:49
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DsContextHolder.getDataSourceType();
    }

}
