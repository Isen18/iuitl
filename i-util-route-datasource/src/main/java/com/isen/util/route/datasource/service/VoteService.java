package com.isen.util.route.datasource.service;

import com.isen.util.route.datasource.annotation.Slave;
import com.isen.util.route.datasource.entity.VoteRecordssMemory;
import com.isen.util.route.datasource.mapper.VoteRecordssMemoryMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Isen
 * @date 2019/4/27 10:01
 * @since 1.0
 */
@Service
public class VoteService {
    @Autowired
    private VoteRecordssMemoryMapper voteRecordssMemoryMapper;

    @Slave
    public List<VoteRecordssMemory> queryPage(int start, int pageSize){
        return voteRecordssMemoryMapper.queryPa(start, pageSize);
    }
}
