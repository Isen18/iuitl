package com.isen.util.test.rest.support;

import com.isen.util.rest.support.annotation.MyAnnotation;

/**
 * @author Isen
 * @date 2018/12/26 11:36
 * @since 1.0
 */
@MyAnnotation("ok")
public class TT {

    public static void main(String[] args) {
        System.out.println(new TT());
    }
}
