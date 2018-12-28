package com.isen.util.rest.support.test;

import com.isen.util.rest.support.annotation.Getter;

/**
 * @author Isen
 * @date 2018/12/20 18:28
 * @since 1.0
 */
@Getter
public class App {
    private String value;

    private String value2;

    public App(String value) {
        this.value = value;
    }

    public static void main(String[] args) {
        App app = new App("it works");
//        System.out.println(app.getValue());
    }
}
