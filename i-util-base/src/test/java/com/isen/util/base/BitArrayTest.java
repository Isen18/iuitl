package com.isen.util.base;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

/**
 * BitArray Tester.
 *
 * @author <isen>
 * @version 1.0
 * @since <pre>11/17/2018</pre>
 */
public class BitArrayTest {
    private BitArray bitArray = null;

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void test1() throws Exception {
        bitArray = new BitArray();

        test(0);
        test(18);
        test(Integer.MAX_VALUE);

        try{
            test(-1);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void test2() throws Exception {
        bitArray = new BitArray(5, 64);

        test(5);
        test(18);
        test(64);

        try{
            test(4);
        }catch (Exception e){
            System.out.println(e);
        }

        try{
            test(65);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void test(int value) throws Exception {
        bitArray.setBit(value);
        assert bitArray.existBit(value);

        bitArray.clearBit(value);
        assert !bitArray.existBit(value);
    }
}
