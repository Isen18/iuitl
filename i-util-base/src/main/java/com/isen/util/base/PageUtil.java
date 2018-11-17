package com.isen.util.base;

import java.util.ArrayList;
import java.util.List;

/**
 * 分片工具，非线程安全
 *
 * @author Isen
 * @date 2018/11/15 16:29
 * @since 1.0
 */
public final class PageUtil {

    private PageUtil(){}

    public static class Iterator<T>{
        List<T> list;
        int pageSize;
        int fromIndex;
        Iterator(List<T> list, int pageSize){
            this.list = list;
            this.pageSize = pageSize;
            this.fromIndex = 0;
        }

        public boolean hasNext(){
            return fromIndex < list.size();
        }

        public List<T> next(){
            int toIndex = fromIndex + pageSize;
            if(toIndex > list.size()){
                toIndex = list.size();
            }

            List<T> result = new ArrayList<>(list.subList(fromIndex, toIndex));
            fromIndex = toIndex;
            return result;
        }
    }

    public static <T> Iterator<T> iterator(List<T> list, int pageSize){
        if(list == null){
            throw new NullPointerException("list is null");
        }

        if(pageSize < 1){
            throw new IllegalArgumentException("pageSize need greater than 0");
        }
        return new Iterator<>(list, pageSize);
    }
}
