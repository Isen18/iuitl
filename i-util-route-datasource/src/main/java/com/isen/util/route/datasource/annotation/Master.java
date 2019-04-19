package com.isen.util.route.datasource.annotation;

import com.isen.util.route.datasource.datasource.DBKeyEn;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Isen
 * @date 2019/4/18 23:54
 * @since 1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Master {
    DBKeyEn value() default DBKeyEn.MASTER;
}
