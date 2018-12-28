package com.isen.util.mybatis.id.inject.aspect;

import com.isen.util.mybatis.id.inject.TestBase;
import com.isen.util.mybatis.id.inject.entity.IdTest;
import com.isen.util.mybatis.id.inject.mapper.IdTestMapper;
import javax.annotation.Resource;
import org.junit.Test;

/**
 * @author Isen
 * @date 2018/12/28 16:37
 * @since 1.0
 */
public class IdAspectTest extends TestBase {
    @Resource
    private IdTestMapper idTestMapper;

    @Test
    public void test(){
        IdTest idTest = new IdTest();
        idTest.setName("isen");
        idTestMapper.insert(idTest);

        System.out.println("id=" + idTest.getId());
    }
}
