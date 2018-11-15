package com.isen.util.retry;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Isen
 * @date 2018/11/2 0:00
 * @since 1.0
 */
@SpringBootApplication
//@ComponentScan("com.isen.util.retry")
@MapperScan(basePackages={"com.isen.util.retry.mapper"})
public class Application {
    private static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
//        new SpringApplicationBuilder(Application.class).profiles("default").build(args).run(args);
        SpringApplication.run(Application.class, args);
    }
}
