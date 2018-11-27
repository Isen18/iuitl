package com.isen.util.retry.mapper;

import com.isen.util.retry.pojo.RetryInfo;
import com.isen.util.retry.pojo.RetryInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RetryInfoMapper {
    long countByExample(RetryInfoExample example);

    int deleteByExample(RetryInfoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(RetryInfo record);

    int insertSelective(RetryInfo record);

    List<RetryInfo> selectByExample(RetryInfoExample example);

    RetryInfo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") RetryInfo record, @Param("example") RetryInfoExample example);

    int updateByExample(@Param("record") RetryInfo record, @Param("example") RetryInfoExample example);

    int updateByPrimaryKeySelective(RetryInfo record);

    int updateByPrimaryKey(RetryInfo record);
}