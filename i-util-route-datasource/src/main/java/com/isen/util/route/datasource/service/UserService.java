package com.isen.util.route.datasource.service;

import com.isen.util.route.datasource.annotation.Slave;
import com.isen.util.route.datasource.entity.User;
import com.isen.util.route.datasource.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Isen
 * @date 2019/4/18 22:35
 * @since 1.0
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Slave
    public User query(Long userId){
        return userMapper.selectByPrimaryKey(userId);
    }

    public long add(User user){
        return userMapper.insert(user);
    }
}
