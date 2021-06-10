package com.example.demo.bll.handle;

import com.example.demo.web.domain.OrderDTO;

/**
 * @author lk
 * @date 2020/4/30
 */
public abstract class AbstractHandle {

    abstract public String handle(OrderDTO dto);
}
