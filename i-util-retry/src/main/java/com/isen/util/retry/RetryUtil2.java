package com.isen.util.retry;

import com.google.gson.Gson;
import com.isen.util.retry.mapper.RetryInfoMapper;
import javax.annotation.Resource;

/**
 * @author Isen
 * @date 2018/12/12 19:48
 * @since 1.0
 */
public class RetryUtil2 {
    @Resource
    private RetryInfoMapper retryInfoMapper;

    private Gson gson = new Gson();

    public void execute(Code code){
        code.execute();
    }
}

@FunctionalInterface
interface Code{

    /**
     * 执行代码
     */
    void execute();
}