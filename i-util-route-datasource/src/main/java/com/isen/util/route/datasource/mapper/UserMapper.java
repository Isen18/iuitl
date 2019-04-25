package com.isen.util.route.datasource.mapper;

import com.isen.util.route.datasource.entity.User;
import com.isen.util.route.datasource.entity.UserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    long countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(Long id);

    User mySelectByPrimaryKey(@Param("id") Long id);

    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    List<User> mySelect(@Param("id")Long id, @Param("name")String name);

    List<User> mySelect2(@Param("id")Long id, @Param("user") User user);
}