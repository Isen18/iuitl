package com.isen.util.route.datasource.config;

import com.isen.util.route.datasource.datasource.DataSourceAspect2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author Isen
 * @date 2019/4/18 23:42
 * @since 1.0
 */
@Configuration
@EnableAspectJAutoProxy
public class AspectConfig {
    @Bean
    public DataSourceAspect2 dataSourceAspect2(){
        return new DataSourceAspect2();
    }
}
