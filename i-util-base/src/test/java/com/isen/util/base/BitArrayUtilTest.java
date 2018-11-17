package com.isen.util.base;

import com.isen.util.base.BitArrayUtil.SynchronizedBitArray;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * BitArrayUtil Tester.
 *
 * @author <isen>
 * @version 1.0
 * @since <pre>11/17/2018</pre>
 */
public class BitArrayUtilTest {
    private String bitArrayName = "id";

    private int value = 11;
    private int value2 = 12;

    private SynchronizedBitArray synchronizedBitArray;

    @Before
    public void before() throws Exception {
        testSynchronizedBitArray();
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: put(String name, BitArray bitArray)
     */
    @Test
    public void testPut() throws Exception {
        BitArray bitArray = new BitArray();
        BitArrayUtil.put(bitArrayName, bitArray);
    }

    /**
     * Method: get(String name)
     */
    @Test
    public void testGet() throws Exception {
        assert BitArrayUtil.get(bitArrayName) != null;
    }

    /**
     * Method: containsKey(String name)
     */
    @Test
    public void testContainsKey() throws Exception {
        assert BitArrayUtil.containsKey(bitArrayName);
    }

    /**
     * Method: setBit(String name, int value)
     */
    @Test
    public void testSetBitForNameValue() throws Exception {
        BitArrayUtil.setBit(bitArrayName, value);
    }

    /**
     * Method: clearBit(String name, int value)
     */
    @Test
    public void testClearBitForNameValue() throws Exception {
        BitArrayUtil.clearBit(bitArrayName, value);
    }

    /**
     * Method: existBit(String name, int value)
     */
    @Test
    public void testExistBitForNameValue() throws Exception {
        BitArrayUtil.existBit(bitArrayName, value);
    }

    /**
     * Method: synchronizedBitArray(BitArray bitArray)
     */
    @Test
    public void testSynchronizedBitArray() throws Exception {
        BitArrayUtil.setBit(bitArrayName, value2);
        synchronizedBitArray = BitArrayUtil.synchronizedBitArray(BitArrayUtil.get(bitArrayName));
    }

    /**
     * Method: setBit(int value)
     */
    @Test
    public void testSetBitValue() throws Exception {
        synchronizedBitArray.setBit(value2);
    }

    /**
     * Method: existBit(int value)
     */
    @Test
    public void testExistBitValue() throws Exception {
        synchronizedBitArray.existBit(value2);
    }

    /**
     * Method: clearBit(int value)
     */
    @Test
    public void testClearBitValue() throws Exception {
        synchronizedBitArray.clearBit(value2);
    }
}
