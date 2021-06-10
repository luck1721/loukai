package com.example.demo.bll.util;

import java.util.UUID;

/**
 * @author lk
 * @date 2020/5/5
 */
public class UUIDUtils {

    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
