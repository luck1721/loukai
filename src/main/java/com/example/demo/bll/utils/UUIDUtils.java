package com.example.demo.bll.utils;

import java.text.DecimalFormat;
import java.util.UUID;

/**
 * @author lk
 * @date 2020/5/5
 */
public class UUIDUtils {

    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    //定义方法
    public static String Chufa(int a,int b) {
        //“0.00000000”确定精度
        DecimalFormat dF = new DecimalFormat("0.00000000");
        return dF.format((float)a/b);
    }
    public static void main(String[] args) {
        DecimalFormat dF = new DecimalFormat("0.00");
        System.out.println((float)3/5);
    }
}
