package com.isen.util.base;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 位数组工具，非线程安全
 *
 * @author Isen
 * @date 2018/11/16 16:51
 * @since 1.0
 */
public class BitArrayUtil {

    /**
     * 位数组map
     */
    private static Map<String, BitArray> bitArrayMap = new HashMap<>();

    private BitArrayUtil() {}

    /**
     * 获取位数组
     *
     * @param name 位数组名
     * @return 位数组
     */
    public static BitArray get(String name) {
        return bitArrayMap.get(name);
    }

    /**
     * 设置位数组
     *
     * @param name 位数组名
     * @param bitArray 位数组
     * @return 位数组名之前关联的位数组
     */
    public static BitArray put(String name, BitArray bitArray) {
        return bitArrayMap.put(name, bitArray);
    }

    /**
     * 是否存在位数组
     *
     * @param name 位数组名
     * @return true:存在，false:不存在
     */
    public static boolean containsKey(String name) {
        return bitArrayMap.containsKey(name);
    }

    /**
     * 位设置
     *
     * @param name 位数组名
     * @param value 位对应的数值
     */
    public static void setBit(String name, int value) {
        BitArray bitArray = bitArrayMap.get(name);
        if (bitArray == null) {
            bitArray = new BitArray();
            bitArrayMap.put(name, bitArray);
        }

        bitArray.setBit(value);
    }

    /**
     * 位清除
     *
     * @param name 位数组名
     * @param value 位对应的数值
     */
    public static void clearBit(String name, int value) {
        BitArray bitArray = bitArrayMap.get(name);
        if (bitArray == null) {
            return;
        }

        bitArray.clearBit(value);
    }

    /**
     * 位是否存在
     *
     * @param name 位数组名
     * @param value 位对应的数值
     * @return true:存在，false:不存在
     */
    public static boolean existBit(String name, int value) {
        BitArray bitArray = bitArrayMap.get(name);
        if (bitArray == null) {
            return false;
        }

        return bitArray.existBit(value);
    }

    /**
     * BitArray的同步类
     */
    static class SynchronizedBitArray {

        private BitArray bitArray;
        private Lock lock = new ReentrantLock();

        public SynchronizedBitArray(BitArray bitArray) {
            this.bitArray = bitArray;
        }

        public void setBit(int value) {
            lock.lock();
            try {
                bitArray.setBit(value);
            } finally {
                lock.unlock();
            }
        }

        public void clearBit(int value) {
            lock.lock();
            try {
                bitArray.clearBit(value);
            } finally {
                lock.unlock();
            }
        }

        public boolean existBit(int value) {
            lock.lock();
            try {
                return bitArray.existBit(value);
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * 获取bitArray的同步类(相对线程安全)
     *
     * @param bitArray 位数组
     * @return bitArray的同步类
     * @throws NullPointerException 如果bitArray为null
     */
    public static SynchronizedBitArray synchronizedBitArray(BitArray bitArray) {
        if (bitArray == null) {
            throw new NullPointerException("bitArray not null");
        }
        return new SynchronizedBitArray(bitArray);
    }

}
