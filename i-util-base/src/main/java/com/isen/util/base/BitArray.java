package com.isen.util.base;

/**
 * 位数组，可以存储位于[0~0x7fffffff(Integer.MAX_VALUE, 2147483647)]间的数值，非线程安全
 *
 * <p>如果minValue=0, maxVavlue=Integer.MAX_VALUE, 数值约占内存268MB</p>
 * @author Isen
 * @date 2018/11/16 16:53
 * @since 1.0
 */
public class BitArray {

    private final int SHIFT = 5;

    private final int MASK = 0x1f;
    /**
     * bits存储位
     */
    private int bits[];
    /**
     *
     */
    private int minValue = 0;
    /**
     * bits容量(位数组存储的最大值，位数组的长度 - 1)
     */
    private int maxValue = 0;

    /**
     * 构造函数
     * <p>最小存储数值为0, 最大存储数值为{@code Integer.MAX_VALUE}</p>
     */
    public BitArray(){
        this(Integer.MAX_VALUE);
    }

    /**
     * 构成函数
     * @param maxValue 位数组的最大存储数值，实际可存储的最大数值>=maxValue，实际可存储的最大数值为{@code getMaxValue()}返回值。
     */
    public BitArray(int maxValue){
        this(0, maxValue);
    }

    /**
     * 构造函数
     * @param minValue 位数组的最小存储数值。
     * @param maxValue 位数组的最大存储数值。
     * @throws IllegalArgumentException 如果minValue小于0
     */
    public BitArray(int minValue, int maxValue){
        if(minValue < 0){
            throw new IllegalArgumentException("minValue less than 0");
        }
        int diff = maxValue - minValue;
        int bitsLen = (diff >> SHIFT) + 1;
        bits = new int[bitsLen];
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    /**
     * 获取位数组可存储的最小值
     * @return 可存储的最小值
     */
    public int getMinValue(){
        return minValue;
    }

    /**
     * 获取位数组可存储的最大值
     * @return 可存储的最大值，超过{@code Integer.MAX_VALUE}
     */
    public int getMaxValue(){
        return maxValue;
    }

    /**
     * 位设置
     * @param value 位对应的数值
     * @throws IllegalArgumentException 如果value小于{@code minValue}或者大于{@code maxValue}
     */
    public void setBit(int value){
        value = convert(value);

        bits[value >> SHIFT] |= (1 << (value & MASK));
    }

    /**
     * 位清除
     * @param value 位对应的数值
     * @throws IllegalArgumentException 如果value小于{@code minValue}或者大于{@code maxValue}
     */
    public void clearBit(int value){
        value = convert(value);

        bits[value >> SHIFT] &= ~(1 << (value & MASK));
    }

    /**
     * 位是否存在
     * @param value 位对应的数值
     * @return true:存在，false:不存在
     * @throws IllegalArgumentException 如果value小于{@code minValue}或者大于{@code maxValue}
     */
    public boolean existBit(int value){
        value = convert(value);
        return (bits[value >> SHIFT] & (1 << (value & MASK))) != 0;
    }

    /**
     * 位设置,相当于方法{@code setBit(int value)}
     * @param value 位对应的数值
     * @param count 位对应的数量，本类忽略
     */
    public void setBit(int value, int count){
        setBit(value);
    }

    /**
     * 获取位对应的数量
     * @param value 位对应的数值
     * @return 数量
     */
    public int getCount(int value){
        if(existBit(value)){
            return 1;
        }

        return 0;
    }

    private final void checkValue(int value){
        if(value < minValue || value > maxValue){
            throw new IllegalArgumentException(String.format("%d less than %d or greter than %d", value, minValue, maxValue));
        }
    }

    private final int convert(int value){
        checkValue(value);
        return value - minValue;
    }
}
