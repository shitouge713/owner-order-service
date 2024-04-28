package com.owner.order.service.impl;

import com.owner.order.service.CreateOrderHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 黑名单规则
 */

@Component
@Slf4j
@Order(1)
public class BlacklistHandler implements CreateOrderHandler {
    @Override
    public boolean canHandle() {
        return false;
    }

    @Override
    public void beforeToDB() {
        log.info("BlacklistHandler,判断用户是否命中黑名单操作");
    }
}
