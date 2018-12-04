package com.isen.util.base;

import java.util.Collection;
import java.util.List;

/**
 * 集合工具
 *
 * @author Isen
 * @date 2018/12/4 12:10
 * @since 1.0
 */
public class CollectionUtil {
    private CollectionUtil(){}

    /**
     * 将collection元素拼接成字符串，并用separator做分隔符
     * @param collection 待拼接的collection
     * @param separator 分隔符
     * @param <T> 待拼接的collection的元素类型
     * @return 拼接后的字符串
     */
    public static <T> String join(Collection<T> collection, String separator){
        String result = "";
        if(collection == null || collection.isEmpty()){
            return result;
        }

        if(separator == null){
            separator = "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        for(T t : collection){
            stringBuilder.append(t + separator);
        }

        result = stringBuilder.toString();
        if(separator.length() > 0 && result.endsWith(separator)){
            result = result.substring(0, result.length() - separator.length());
        }

        return result;
    }

    /**
     * 根据collection拼接字符串，用separator做分隔符
     * @param collection 待拼接的collection
     * @param consumer 拼接元素的获取器(从collection元素获取需要拼接的元素) null:直接用collection元素拼接 {@link #join}
     * @param separator 分隔符
     * @param <T> collection元素类型
     * @param <R> 拼接的元素类型
     * @return 拼接后的字符串
     */
    public static <T, R> String join(Collection<T> collection, Consumer<T, R> consumer, String separator){
        String result = "";
        if(collection == null || collection.isEmpty()){
            return result;
        }

        if(separator == null){
            separator = "";
        }

        if(consumer == null){
            consumer = tmp -> (R)tmp;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for(T t : collection){
            stringBuilder.append(consumer.get(t) + separator);
        }

        result = stringBuilder.toString();
        if(separator.length() > 0 && result.endsWith(separator)){
            result = result.substring(0, result.length() - separator.length());
        }

        return result;
    }

    /**
     * 将separator插入collection的两两元素之间
     *
     * <p>例如，collection=[1,2,3] separator=6，将返回[1,6,2,6,3]
     * @param collection 待插入的collection
     * @param separator 需要插入的元素
     * @param <T> list的元素类型
     * @return 完成插入separator的collection
     */
    public static <T> Collection<T> join(Collection<T> collection, T separator){
        throw new UnsupportedOperationException();
    }

    public interface Consumer<T, R>{
        R get(T t);
    }
}


