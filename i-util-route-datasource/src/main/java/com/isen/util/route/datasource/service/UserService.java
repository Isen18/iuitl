package com.isen.util.route.datasource.service;

import com.isen.util.route.datasource.annotation.Slave;
import com.isen.util.route.datasource.entity.User;
import com.isen.util.route.datasource.entity.UserExample;
import com.isen.util.route.datasource.mapper.UserMapper;
import java.util.List;
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

    @Slave
    public User query2(Long userId){
        return userMapper.mySelectByPrimaryKey(userId);
    }

    @Slave
    public List<User> query3(Long userId, String name){
        return userMapper.mySelect(userId, name);
    }

    @Slave
    public List<User> query4(Long userId, String name){
        UserExample example = new UserExample();
        example.createCriteria().andIdEqualTo(userId).andNameEqualTo(name);
        return userMapper.selectByExample(example);
    }

    @Slave
    public List<User> query5(Long userId, String name){
        User user = new User();
        user.setId(userId);
        user.setName(name);
        return userMapper.mySelect2(userId, user);
    }

    public long add(User user){
        return userMapper.insert(user);
    }
}
