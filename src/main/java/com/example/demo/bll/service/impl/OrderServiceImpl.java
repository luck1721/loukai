package com.example.demo.bll.service.impl;

import com.example.demo.bll.handle.AbstractHandle;
import com.example.demo.bll.handle.HandleContext;
import com.example.demo.bll.service.OrderService;
import com.example.demo.web.domain.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lk
 * @date 2020/4/30
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private HandleContext handleContext;
    @Override
    public String handle(OrderDTO dto) {
        AbstractHandle handle = handleContext.getInstance(dto.getType());
        return handle.handle(dto);
    }
}
