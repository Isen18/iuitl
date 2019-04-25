package com.isen.util.monitor.sql.plugin;

import java.sql.Statement;
import java.util.Properties;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;

/**
 * @author Isen
 * @date 2019/4/24 22:51
 * @since 1.0
 */
@Intercepts({
        @Signature(
                type = StatementHandler.class, //拦截的目标对象类型，可以是Executor、StatementHandler、PameterHandler和ResultSetHandler
                method = "query", //对于query进行拦截
                args = {Statement.class, ResultHandler.class})})
public class SlowQueryPlugin implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //代理对象Plugin中的query方法执行时调用，可以在此处进行拦截处理
        System.out.println(String.format("intercept target=%s, method=%s", invocation.getTarget(), invocation.getMethod()));
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        //如果target是StatementHandler，则返回代理对象Plugin。否则直接返回target
        System.out.println("plugin target=" + target.getClass());
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        //获取插件配置的属性
        String name = properties.getProperty("name", "isenDefault");
        System.out.println(String.format("setProperties name=%s", name));
    }
}
