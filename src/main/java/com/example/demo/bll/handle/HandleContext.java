package com.example.demo.bll.handle;

import com.example.demo.bll.utils.SpringUtil;

import java.util.Map;

/**
 * @author lk
 * @date 2020/4/30
 */
public class HandleContext {
    private Map<String,Class> map;

    public HandleContext(Map<String,Class> map) {
        this.map = map;
    }

    public AbstractHandle getInstance(String type) {
        Class clazz = map.get(type);
        if (clazz == null) {
            throw new IllegalArgumentException("not found");
        }
        return (AbstractHandle) SpringUtil.getBean(clazz);

    }
}
