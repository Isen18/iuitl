package com.isen.util.base;

import java.util.HashMap;

/**
 * 位数组扩展，每个位带有数量，
 *
 * <p>注意：数量信息可能占用大量内存</p>
 *
 * @author Isen
 * @date 2018/11/17 11:52
 * @since 1.0
 */
public class BitArrayWithCount extends BitArray {

    /**
     * 存储位的数量map
     */
    private HashMap<Integer, Integer> countMap = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBit(int value){
        super.setBit(value);
        countMap.remove(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearBit(int value){
        super.clearBit(value);
        countMap.remove(value);
    }

    /**
     * 位设置
     * @param value 位对应的数值
     * @param count 位对应的数量
     */
    @Override
    public void setBit(int value, int count){
        if(count < 1){
            throw new IllegalArgumentException("count less than 1");
        }

        super.setBit(value);
        if(count > 1){
            //只有数量大于1才存于countMap，因为位数组中的位本身可以表示数量1
            countMap.put(value, count);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount(int value){
        if(!existBit(value)){
            return 0;
        }

        Integer count = countMap.get(value);
        if(count == null){
            return 1;
        }

        return count;
    }
}
