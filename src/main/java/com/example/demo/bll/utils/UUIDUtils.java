package com.example.demo.bll.utils;

import java.util.UUID;

/**
 * @author lk
 * @date 2020/5/5
 */
public class UUIDUtils {

    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static void main(String[] args) {
        System.out.println(String.valueOf(Long.parseLong("9020", 16)));
    }
}
