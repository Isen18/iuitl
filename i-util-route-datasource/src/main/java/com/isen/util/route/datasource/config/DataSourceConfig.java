package com.isen.util.route.datasource.config;

import com.isen.util.route.datasource.datasource.DBKeyEn;
import com.isen.util.route.datasource.datasource.RoutingDataSource;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * @author Isen
 * @date 2019/4/19 22:04
 * @since 1.0
 */
@Configuration
@MapperScan(basePackages = {
        "com.isen.util.route.datasource.mapper"}, sqlSessionFactoryRef = DataSourceConfig.SESSION_FACTORY_NAME)
public class DataSourceConfig {

    public static final String DATASOURCE_NAME = "routingDataSource";
    public static final String MASTER_DATASOURCE_NAME = "masterDataSource";
    public static final String SLAVE_DATASOURCE_NAME = "slaveDataSource";
    public static final String TRANSACTION_MANAGER_NAME = "txManager";
    public static final String SESSION_FACTORY_NAME = "sessionFactory";
    public static final String JDBC_TEMPLATE_NAME = "mallReturnDbJdbcTemplate";

    @Value("${mybatis.mapper-locations}")
    private String mybatisMapperLocations;

    @Value("${mybatis.config-locations}")
    private String mybatisConfigLocations;

    private RoutingDataSource routingDataSource = null;

    private DataSource slaveDataSource = null;
    private DataSource masterDataSource = null;

    @Bean(name = DATASOURCE_NAME)
    public DataSource initDataSource() {
        routingDataSource = new RoutingDataSource();
        masterDataSource = initMasterDataSource();
        slaveDataSource = initSlaveDataSource();

        Map<Object, Object> targetDataSources = new HashMap<>(2);
        targetDataSources.put(DBKeyEn.MASTER.getMean(), masterDataSource);
        targetDataSources.put(DBKeyEn.SLAVE.getMean(), slaveDataSource);

        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(masterDataSource);
        return routingDataSource;
    }

    @Bean(name = MASTER_DATASOURCE_NAME)
    @ConfigurationProperties(prefix = "jdbc.master")
    public DataSource initMasterDataSource() {
        return  DataSourceBuilder.create().build();
    }

    @Bean(name = SLAVE_DATASOURCE_NAME)
    @ConfigurationProperties(prefix = "jdbc.slave")
    public DataSource initSlaveDataSource() {
        return DataSourceBuilder.create().type(org.apache.tomcat.jdbc.pool.DataSource.class).build();
//        org.apache.tomcat.jdbc.pool.DataSource dataSource = DataSourceBuilder.create().type(org.apache.tomcat.jdbc.pool.DataSource.class).build();
//        dataSource.setJdbcInterceptors(SlowQueryReport.class.getName());
//        dataSource.setJdbcInterceptors(SlowQueryReportJmx.class.getName());
//        return dataSource;
    }


//    @Bean(name = SLAVE_DATASOURCE_NAME)
//    @ConfigurationProperties(prefix = "jdbc.slave")
//    public DataSource initSlaveDataSource(DataSourceProperties dataSourceProperties) {
//        return dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
//    }

    @Bean(name = TRANSACTION_MANAGER_NAME)
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(initDataSource());
    }

    @Bean(name = SESSION_FACTORY_NAME)
    public SqlSessionFactory initSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(initDataSource());
        Resource[] mapperLocations = new PathMatchingResourcePatternResolver().getResources(mybatisMapperLocations);
        Resource[] configs = new PathMatchingResourcePatternResolver().getResources(mybatisConfigLocations);

        sessionFactory.setMapperLocations(mapperLocations);
        sessionFactory.setConfigLocation(configs[0]);
        return sessionFactory.getObject();
    }

    @Bean(name = JDBC_TEMPLATE_NAME)
    public JdbcTemplate initJdbcTemplate() {
        return new JdbcTemplate(initDataSource());
    }
}
