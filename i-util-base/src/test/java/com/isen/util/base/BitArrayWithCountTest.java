package com.isen.util.base;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

/**
 * BitArrayWithCount Tester.
 *
 * @author <isen>
 * @version 1.0
 * @since <pre>11/17/2018</pre>
 */
public class BitArrayWithCountTest {
    private BitArrayWithCount bitArrayWithCount;

    @Before
    public void before() throws Exception {
        bitArrayWithCount = new BitArrayWithCount();
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void test(){
        int value = 12;
        int count = 18;
        bitArrayWithCount.setBit(value, count);
        assert bitArrayWithCount.existBit(value);
        assert bitArrayWithCount.getCount(value) == count;

        bitArrayWithCount.clearBit(value);
        assert !bitArrayWithCount.existBit(value);
    }
}
