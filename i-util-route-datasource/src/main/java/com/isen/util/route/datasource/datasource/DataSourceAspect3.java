package com.isen.util.route.datasource.datasource;

import com.isen.util.route.datasource.annotation.Master;
import com.isen.util.route.datasource.annotation.Slave;
import java.lang.reflect.Method;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Isen
 * @date 2019/4/17 22:29
 * @since 1.0
 */
public class DataSourceAspect3 implements MethodInterceptor {

    // TODO isen 2019/4/19 动态配置
    private Boolean routing = true;

    public void setRouting(Boolean routing) {
        this.routing = routing;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        //数据源切换
        routingDatasource(invocation);

        return invocation.proceed();
    }

    private void routingDatasource(MethodInvocation invocation) {
        if(!routing){
            return;
        }

        Method method = invocation.getMethod();
        if (method.isAnnotationPresent(Master.class)){
            RoutingDataSourceHolder.markDBKey(method.getAnnotation(Master.class).value());
        } else if (method.isAnnotationPresent(Slave.class)) {
            RoutingDataSourceHolder.markDBKey(method.getAnnotation(Slave.class).value());
        }
    }
}
