package com.isen.util.route.datasource.test;

import com.isen.util.route.datasource.entity.User;
import com.isen.util.route.datasource.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

/**
 * @author Isen
 * @date 2019/4/18 22:07
 * @since 1.0
 */
public class TestRoutingDataSource {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                "config/application-context.xml");
        UserService userService = applicationContext.getBean(UserService.class);

//        User user = new User();
//        user.setName("isen");
//        userService.add(user);

        User user = userService.query(2L);
        Assert.isNull(user, "uid[2] = null");

        User user2 = userService.query(1L);
        Assert.isNull(user2, "uid[1] = null");
    }
}
