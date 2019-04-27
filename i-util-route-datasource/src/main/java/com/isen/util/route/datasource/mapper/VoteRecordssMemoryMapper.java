package com.isen.util.route.datasource.mapper;

import com.isen.util.route.datasource.entity.VoteRecordssMemory;
import com.isen.util.route.datasource.entity.VoteRecordssMemoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface VoteRecordssMemoryMapper {
    long countByExample(VoteRecordssMemoryExample example);

    int deleteByExample(VoteRecordssMemoryExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(VoteRecordssMemory record);

    int insertSelective(VoteRecordssMemory record);

    List<VoteRecordssMemory> selectByExample(VoteRecordssMemoryExample example);

    VoteRecordssMemory selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") VoteRecordssMemory record, @Param("example") VoteRecordssMemoryExample example);

    int updateByExample(@Param("record") VoteRecordssMemory record, @Param("example") VoteRecordssMemoryExample example);

    int updateByPrimaryKeySelective(VoteRecordssMemory record);

    int updateByPrimaryKey(VoteRecordssMemory record);

    List<VoteRecordssMemory> queryPa(@Param("start") int start, @Param("pageSize") int pageSize);
}