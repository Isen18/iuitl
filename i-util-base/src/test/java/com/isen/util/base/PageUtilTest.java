package com.isen.util.base;

import com.isen.util.base.PageUtil.Iterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

/**
 * @author Isen
 * @date 2018/11/15 17:00
 * @since 1.0
 */
public class PageUtilTest {
    @Test
    public void pageUtilTest(){
        List<Integer> ids = Arrays.asList(1, 2, 3, 4, 5, 6);
//        System.out.println(ids.subList(1, 1));
//        List<Integer> ids = new ArrayList<>();
//        Iterator iterator =  PageUtil.iterator(ids, 1);
//        Iterator iterator =  PageUtil.iterator(ids, 2);
//        Iterator iterator =  PageUtil.iterator(ids, 6);
        Iterator iterator =  PageUtil.iterator(ids, 7);
        while (iterator.hasNext()){
            System.out.println("=======");
            List<Integer> idsTmp = iterator.next();
            //根据 idsTmp 进行业务处理
            System.out.println(idsTmp);
        }
    }
}
