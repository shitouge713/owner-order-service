package com.owner.order.service.impl;

import com.owner.order.service.CreateOrderHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 库存规则
 */
@Component
@Slf4j
@Order(7)
public class RepertoryHandler implements CreateOrderHandler {
    @Override
    public boolean canHandle() {
        return true;
    }

    @Override
    public void beforeToDB() {
        log.info("RepertoryHandler,库存逻辑");
    }
}
