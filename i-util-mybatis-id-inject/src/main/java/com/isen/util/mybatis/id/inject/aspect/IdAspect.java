package com.isen.util.mybatis.id.inject.aspect;

import com.isen.util.mybatis.id.inject.bridge.IdGenerator;
import com.isen.util.mybatis.id.inject.exception.IdException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import javax.annotation.Resource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author Isen
 * @date 2018/12/27 15:31
 * @since 1.0
 */
@Aspect
public class IdAspect {

    private static final String ID = "id";

    @Resource
    private IdGenerator idGenerator;

    public IdAspect(String pointCutExpress){

    }

    // TODO isen 2018/12/28 将配置提取出去
    @Pointcut("execution(* *com.isen.util.mybatis.id.inject.mapper.*.insert*(..))")
    public void idInjectPointcut(){}

    @Before("idInjectPointcut()")
    public void idInject(JoinPoint joinPoint) {
        Object[] objects = joinPoint.getArgs();
        // TODO isen 2018/12/28 提供忽略注解
        if(objects.length < 1){
            throw new IdException(String.format("mapper的插入方法没有参数, joinPoint=%s", joinPoint.toShortString()));
        }

        //获取第一个参数(默认)
        // TODO isen 2018/12/28 提供注解进行自定义
        Object record = objects[0];
        Class<?> recordType = record.getClass();
        Field idField;
        try {
            idField = recordType.getDeclaredField(ID);
        } catch (NoSuchFieldException e) {
            throw new IdException(String.format("class=%s, 不存在主键id字段", recordType.getName()));
        }

        try {
            Long id = idGenerator.generateId();
            if(id == null){
                throw new IdException(String.format("主键生成失败, joinPoint=%s", joinPoint.toShortString()));
            }
            AccessibleObject.setAccessible(new AccessibleObject[]{idField}, true);
            idField.set(record, id);
        } catch (IllegalAccessException e) {
            throw new IdException(e.getMessage(), e);
        }
    }

}
