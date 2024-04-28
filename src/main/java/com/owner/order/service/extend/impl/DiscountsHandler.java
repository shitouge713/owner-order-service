package com.owner.order.service.extend.impl;

import com.owner.order.service.extend.CreateOrderHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 促销规则
 */
@Component
@Slf4j
@Order(3)
public class DiscountsHandler implements CreateOrderHandler {
    @Override
    public boolean canHandle() {
        return true;
    }

    @Override
    public void beforeToDB() {
        log.info("DiscountsHandler,促销逻辑");
    }
}
