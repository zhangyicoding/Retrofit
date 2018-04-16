package com.estyle.retrofit.util;

public class MyLog {

    public static void e(Object obj) {
        e("", obj);
    }

    public static void e(String tag, Object obj) {
        System.out.println(obj);
    }

}
