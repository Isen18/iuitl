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

    public static void markDBType(DBTypeEn dbTypeEn){
        holder.set(dbTypeEn.getMean());
    }

}
