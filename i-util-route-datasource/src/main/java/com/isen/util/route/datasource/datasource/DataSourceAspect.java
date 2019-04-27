package com.isen.util.route.datasource.datasource;

import org.aspectj.lang.JoinPoint;
import org.springframework.util.StringUtils;

/**
 * @author Isen
 * @date 2019/4/17 22:29
 * @since 1.0
 */
public class DataSourceAspect {

    public void routingDataSource(JoinPoint point) {
        String methodName = point.getSignature().getName();
        if (isSlave(methodName)) {
            RoutingDataSourceHolder.markDBKey(DBKeyEn.SLAVE);
        } else {
            RoutingDataSourceHolder.markDBKey(DBKeyEn.MASTER);
        }
    }


    private Boolean isSlave(String methodName) {
        // 方法名以query、find、get开头的方法名走从库
        boolean re = StringUtils.startsWithIgnoreCase(methodName, "queryPa");
        re |= StringUtils.startsWithIgnoreCase(methodName, "find");
        re |= StringUtils.startsWithIgnoreCase(methodName, "get");
        return re;
    }
}
