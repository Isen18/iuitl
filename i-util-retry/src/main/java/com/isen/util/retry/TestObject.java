package com.isen.util.retry;

/**
 * @author Isen
 * @date 2018/11/1 16:10
 * @since 1.0
 */
public class TestObject {
    public String getHello(String name, Integer age){
        return "hello " + "name=" + name + " age=" + age;
    }

    public void printHello(){
        System.out.println("hello");
    }
}
