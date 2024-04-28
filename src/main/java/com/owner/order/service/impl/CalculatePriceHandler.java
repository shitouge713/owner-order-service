package com.owner.order.service.impl;

import com.owner.order.service.CreateOrderHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 算价规则
 */
@Component
@Slf4j
@Order(2)
public class CalculatePriceHandler implements CreateOrderHandler {
    @Override
    public boolean canHandle() {
        return true;
    }

    @Override
    public void beforeToDB() {
        log.info("CalculatePriceHandler,算法逻辑");
    }
}
