package com.isen.util.mybatis.id.inject.config;

import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

/**
 * @author Isen
 * @date 2018/12/26 16:53
 * @since 1.0
 */
@Configuration
@MapperScan(basePackages = "com.isen.util.mybatis.id.inject.mapper", sqlSessionFactoryRef=DataSourceConfig.SQL_SESSION_FACTORY)
public class DataSourceConfig {

    public static final String SQL_SESSION_FACTORY = "sessionFactory";

    @Profile("development")
    @Bean
    public DataSource dataSource(){
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:db/schema.sql")
                .build();
//                .addScript("classpath:db/test-data.sql")
    }

    @Bean(name = SQL_SESSION_FACTORY)
    @Primary
    public SqlSessionFactory initSqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        return sessionFactory.getObject();
    }
}
