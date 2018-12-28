package com.isen.util.mybatis.id.inject;

import com.isen.util.mybatis.id.inject.config.AppContext;
import com.isen.util.mybatis.id.inject.config.AspectConfig;
import com.isen.util.mybatis.id.inject.config.DataSourceConfig;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Isen
 * @date 2018/12/28 16:20
 * @since 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={DataSourceConfig.class, AspectConfig.class, AppContext.class})
@ActiveProfiles("development")
public class TestBase {

}
