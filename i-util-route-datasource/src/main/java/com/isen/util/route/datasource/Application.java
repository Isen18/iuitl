package com.isen.util.route.datasource;

import com.isen.util.route.datasource.entity.User;
import com.isen.util.route.datasource.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.Assert;

/**
 * @author Isen
 * @date 2019/4/18 22:07
 * @since 1.0
 */
@SpringBootApplication
@EnableTransactionManagement
//@ImportResource(locations = "classpath:config/*")
public class Application {

    private static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = new SpringApplication(Application.class).run(args);
        UserService userService = applicationContext.getBean(UserService.class);

//        User user = new User();
//        user.setName("isen");
//        userService.add(user);

        User user = userService.query(2L);
        Assert.isTrue(user != null, "uid[2] = null");

        User user2 = userService.query(1L);
        Assert.isTrue(user2 == null, "uid[1] != null");

        logger.info("ok");
    }
}
