package com.isen.util.mybatis.id.inject.mapper;

import com.isen.util.mybatis.id.inject.entity.IdTest;
import com.isen.util.mybatis.id.inject.entity.IdTestExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface IdTestMapper {
    long countByExample(IdTestExample example);

    int deleteByExample(IdTestExample example);

    int deleteByPrimaryKey(Long id);

    int insert(IdTest record);

    int insertSelective(IdTest record);

    List<IdTest> selectByExample(IdTestExample example);

    IdTest selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") IdTest record, @Param("example") IdTestExample example);

    int updateByExample(@Param("record") IdTest record, @Param("example") IdTestExample example);

    int updateByPrimaryKeySelective(IdTest record);

    int updateByPrimaryKey(IdTest record);
}