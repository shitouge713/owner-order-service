package com.owner.order.service.extend.impl;

import com.owner.order.service.extend.CreateOrderHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 参数校验规则
 */
@Component
@Slf4j
@Order(6)
public class ParamVerifyHandler implements CreateOrderHandler {
    @Override
    public boolean canHandle() {
        return true;
    }
    @Override
    public void beforeToDB() {
        log.info("ParamVerifyHandler,参数校验逻辑");
    }
}
