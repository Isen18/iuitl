package com.isen.util.route.datasource.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author Isen
 * @date 2019/4/17 22:20
 * @since 1.0
 */
public class RoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        // 使用DynamicDataSourceHolder保证线程安全，并且得到当前线程中的数据源key
        return RoutingDataSourceHolder.getDataSourceKey();
    }
}
