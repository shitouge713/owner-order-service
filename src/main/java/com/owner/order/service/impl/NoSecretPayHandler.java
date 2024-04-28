package com.owner.order.service.impl;

import com.owner.order.service.CreateOrderHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 免密扣规则
 */
@Component
@Slf4j
@Order(5)
public class NoSecretPayHandler implements CreateOrderHandler {
    @Override
    public boolean canHandle() {
        return true;
    }

    @Override
    public void beforeToDB() {
        log.info("MsgPushHandler,免密扣逻辑");
    }
}
