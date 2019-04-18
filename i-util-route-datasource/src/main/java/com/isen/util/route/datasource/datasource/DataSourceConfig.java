//package com.isen.util.route.datasource;
//
//import java.util.HashMap;
//import java.util.Map;
//import javax.annotation.PostConstruct;
//import javax.annotation.Resource;
//import javax.sql.DataSource;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.annotation.MapperScan;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.core.env.Environment;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//
//@Configuration
//@MapperScan(basePackages = {
//        "com.dianwoba.mall.returngoods.mapper"}, sqlSessionFactoryRef = DataSourceConfig.SESSION_FACTORY_NAME)
//public class DataSourceConfig {
//
//    private static final Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);
//
//    public static final String DATASOURC_ENAME = "mallReturnDataSource";
//    public static final String MASTER_DATASOURCE_ENAME = "masterMallReturnDbDataSource";
//    public static final String SLAVE_DATASOURCE_ENAME = "slaveMallReturnDataSource";
//    public static final String JDBC_TEMPLATE_ENAME = "mallReturnDbJdbcTemplate";
//    public static final String TRANSACTION_MANAGER_NAME = "mallReturnDbTransactionManager";
//    public static final String SESSION_FACTORY_NAME = "mallReturnDbSqlSessionFactory";
//
//    @Resource
//    private Environment env;
//
//    private RoutingDataSource dataSource = null;
//
//    private DataSource slaveDataSource = null;
//    private DataSource masterDataSource = null;
//
//    @Bean(name = DATASOURC_ENAME)
//    @Primary
//    public DataSource initDataSource() {
//        if (dataSource != null) {
//            return dataSource;
//        }
//        dataSource = new RoutingDataSource();
//        slaveDataSource = initSlaveDataSource();
//        masterDataSource = initMasterDataSource();
//        Map<Object, Object> targetDataSources = new HashMap<>(2);
//        targetDataSources.put(DBTypeEn.MASTER.getMean(), masterDataSource);
//        targetDataSources.put(DBTypeEn.SLAVE.getMean(), slaveDataSource);
//        dataSource.setTargetDataSources(targetDataSources);
//        dataSource.setDefaultTargetDataSource(masterDataSource);
//        return dataSource;
//    }
//
//    @Bean(name = TRANSACTION_MANAGER_NAME)
//    @Primary
//    public DataSourceTransactionManager transactionManager() {
//        return new DataSourceTransactionManager(initDataSource());
//    }
//
//    @Bean(name = SESSION_FACTORY_NAME)
//    @Primary
//    public SqlSessionFactory initSqlSessionFactory() throws Exception {
//        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
//        sessionFactory.setDataSource(initDataSource());
//        org.springframework.core.io.Resource[] mapperLocation = new PathMatchingResourcePatternResolver()
//                .getResources(env.getProperty("mybatis.mall_return.mapper-locations"));
//        org.springframework.core.io.Resource[] config = new PathMatchingResourcePatternResolver()
//                .getResources(env.getProperty("mybatis.mall_return.config-locations"));
//
//        sessionFactory.setMapperLocations(mapperLocation);
//        sessionFactory.setConfigLocation(config[0]);
//        org.apache.ibatis.logging.LogFactory.useLog4J2Logging();
//        return sessionFactory.getObject();
//    }
//
//    @Bean(name = JDBC_TEMPLATE_ENAME)
//    public JdbcTemplate initJdbcTemplate() {
//        return new JdbcTemplate(initDataSource());
//    }
//
//
//    @Bean(name = MASTER_DATASOURCE_ENAME)
//    @PostConstruct
//    @ConfigurationProperties(prefix = "spring.datasource.mall_return")
//    public DataSource initMasterDataSource() {
//        logger.info("mall return gw master initMasterDataSource****start************");
//        if (masterDataSource == null) {
//            masterDataSource = DataSourceBuilder.create().build();
//        }
//        return masterDataSource;
//    }
//
//    @Bean(name = SLAVE_DATASOURCE_ENAME)
//    @PostConstruct
//    @ConfigurationProperties(prefix = "spring.datasource.slave.mall_return")
//    public DataSource initSlaveDataSource() {
//        logger.info("mall return gw slave initSlaveDataSource****start************");
//        if (slaveDataSource == null) {
//            slaveDataSource = DataSourceBuilder.create().build();
//        }
//
//        return slaveDataSource;
//    }
//
////    /**
////     * 从数据源
////     * @return
////     */
////    @Bean(name = SLAVE_DATA_SOURCE_NAME)
////    @ConfigurationProperties(prefix = "spring.datasource.cluster.clubdb")
////    public DataSource initSlaveDataSource(DataSourceProperties clusterDataSourceProperties) {
////        DataSource slaveDataSource  = null;
////        logger.info("开始初始化从数据库...");
////        if (slaveDataSource == null) {
////            slaveDataSource = clusterDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
////        }
////        return slaveDataSource;
////    }
//}
