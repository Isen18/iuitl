package com.isen.util.mybatis.id.inject.bridge;

import org.springframework.stereotype.Service;

/**
 * @author Isen
 * @date 2018/12/28 17:11
 * @since 1.0
 */
@Service("idGenerator")
public class IdGeneratorAbstract implements IdGenerator {

    @Override
    public <T> T generateId() {
        return (T)new Long(1);
    }
}
