package com.isen.util.route.datasource.plugin;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Isen
 * @date 2019/4/24 22:51
 * @since 1.0
 */
//问题：需要编译sql，时间会不准确
@Intercepts({
        @Signature(
                type = Executor.class, //拦截的目标对象类型，可以是Executor、StatementHandler、ParameterHandler和ResultSetHandler
                method = "query", //对于query进行拦截
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class,
                        CacheKey.class, BoundSql.class}),
        @Signature(
                type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(
                type = Executor.class,
                method = "queryCursor",
                args = {MappedStatement.class, Object.class, RowBounds.class})})
public class SlowQueryPlugin implements Interceptor {
    private final static Logger LOGGER = LoggerFactory.getLogger(SlowQueryPlugin.class);
    private int slowSQLThreshold = 0;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        long begin = System.currentTimeMillis();
        Object result = invocation.proceed();
        long end = System.currentTimeMillis();

        long during = end - begin;
//        if(during < slowSQLThreshold){
//            return result;
//        }

        //超过时间阈值需要记慢查询日志
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        BoundSql boundSql = mappedStatement.getBoundSql(args[1]);

        Configuration configuration = mappedStatement.getConfiguration();

        String sql = sqlOf(configuration, boundSql);
        // TODO isen 2019/4/25 命中行数
//        long hintRowCount = 0;
//        String warn = String.format("sql=[%s], cost=[%dms], hint_row_count=[%d]", sql, during, hintRowCount);
        String warn = String.format("sql=[%s], cost=[%sms]", sql, during);
        LOGGER.warn(warn);
        return result;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        slowSQLThreshold = Integer.parseInt(properties.getProperty("slow_sql_threshold", "100"));
    }

    public String sqlOf(Configuration configuration, BoundSql boundSql) {
        //获取参数
        Object parameterObject = boundSql.getParameterObject();
        //获取参数映射
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();

        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");

        if (parameterMappings.size() > 0 && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", strOf(parameterObject));
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", strOf(obj));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", strOf(obj));
                    }
                }
            }
        }

        return sql;
    }

    private String strOf(Object obj) {
        String value;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(obj) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }

        }
        return value;
    }
}
