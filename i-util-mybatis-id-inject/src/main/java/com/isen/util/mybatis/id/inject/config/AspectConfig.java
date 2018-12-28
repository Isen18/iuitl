package com.isen.util.mybatis.id.inject.config;

import com.isen.util.mybatis.id.inject.aspect.IdAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author Isen
 * @date 2018/12/27 16:11
 * @since 1.0
 */
@Configuration
@EnableAspectJAutoProxy
public class AspectConfig {
    @Bean
    public IdAspect idAspect(){
        return new IdAspect("execution(* *com.isen.util.mybatis.id.inject.mapper.*.insert*(..))");
    }
}
