package com.isen.util.mybatis.id.inject.bridge;

/**
 * id生成器
 *
 * @author Isen
 * @date 2018/12/28 16:09
 * @since 1.0
 */
public interface IdGenerator {

    /**
     * 生成一个Id
     * @return
     */
    <T> T generateId();
}
