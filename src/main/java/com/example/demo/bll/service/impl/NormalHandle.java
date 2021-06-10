package com.example.demo.bll.service.impl;

import com.example.demo.bll.anon.HandleType;
import com.example.demo.bll.handle.AbstractHandle;
import com.example.demo.web.domain.OrderDTO;
import org.springframework.stereotype.Component;

/**
 * @author lk
 * @date 2020/4/30
 */
@Component
@HandleType("1")
public class NormalHandle extends AbstractHandle {
    @Override
    public String handle(OrderDTO dto) {
        return "123";
    }
}
