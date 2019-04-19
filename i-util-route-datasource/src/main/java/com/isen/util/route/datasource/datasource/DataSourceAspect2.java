package com.isen.util.route.datasource.datasource;

import com.isen.util.route.datasource.annotation.Master;
import com.isen.util.route.datasource.annotation.Slave;
import java.lang.reflect.Method;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * @author Isen
 * @date 2019/4/17 22:29
 * @since 1.0
 */
@Aspect
public class DataSourceAspect2{

    // TODO isen 2019/4/19 动态配置
    private Boolean routing = true;

    public void setRouting(Boolean routing) {
        this.routing = routing;
    }

    @Pointcut("execution(* *com.isen.util.route.datasource.service..*.*(..))")
    public void routingPointCut(){}

    @Before("routingPointCut()")
    public void routingDataSource(JoinPoint point) {
        if(!routing){
            return;
        }

        Signature signature = point.getSignature();
        if(signature instanceof MethodSignature){
            Method method = ((MethodSignature)signature).getMethod();

            if (method.isAnnotationPresent(Master.class)){
                RoutingDataSourceHolder.markDBKey(method.getAnnotation(Master.class).value());
            } else if (method.isAnnotationPresent(Slave.class)) {
                RoutingDataSourceHolder.markDBKey(method.getAnnotation(Slave.class).value());
            }
        }
    }
}
