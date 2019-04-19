package com.isen.util.route.datasource.datasource;

/**
 * @author Isen
 * @date 2019/4/17 22:26
 * @since 1.0
 */
public class RoutingDataSourceHolder {

    private static final ThreadLocal<String> holder = new ThreadLocal<>();

    public static String getDataSourceKey() {
        return holder.get();
    }

    /**
     * 标记要使用的数据源
     * @param dbKeyEn 数据源key，对应AbstractRoutingDataSource中的lookupKey
     */
    public static void markDBKey(DBKeyEn dbKeyEn){
        holder.set(dbKeyEn.getMean());
    }

}
