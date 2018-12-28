package com.isen.util.mybatis.id.inject.aspect;

import com.isen.util.mybatis.id.inject.bridge.IdGenerator;
import com.isen.util.mybatis.id.inject.exception.IdException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.annotation.Resource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Isen
 * @date 2018/12/27 15:31
 * @since 1.0
 */
@Aspect
public class IdAspect {

    @Resource
    private IdGenerator idGenerator;

    /**
     * id设置方法名
     */
    private static final String ID_SET_METHOD_NAME = "setId";

    private static final Logger logger = LoggerFactory.getLogger(IdAspect.class);

    @Pointcut("execution(* *com.isen.util.mybatis.id.inject.mapper.*.insert*(..))")
    public void idInjectPointcut(){}

    @Before("idInjectPointcut()")
    public void idInject(JoinPoint joinPoint) {
        Object[] objects = joinPoint.getArgs();
        if(objects.length < 1){
            logger.warn("mapper的插入方法没有参数, joinPoint={}", joinPoint.toShortString());
            return;
        }

        //获取第一个参数
        Object record = objects[0];
        Class<?> clazz = record.getClass();
        try {
            Method method = clazz.getMethod(ID_SET_METHOD_NAME, Long.class);
            Long id = idGenerator.generateId();
            if(id == null){
                logger.error("主键生成失败, joinPoint={}", joinPoint.toShortString());
                throw new IdException("主键生成失败");
            }

                method.invoke(record, id);
        } catch (NoSuchMethodException e) {
            logger.warn(String.format("mapper的插入方法第一个参数没有方法=%s", ID_SET_METHOD_NAME), e);
        } catch (IllegalAccessException e) {
            logger.warn("没有权限", e);
        } catch (InvocationTargetException e) {
            throw new IdException(e.getMessage(), e);
        }
    }

}
