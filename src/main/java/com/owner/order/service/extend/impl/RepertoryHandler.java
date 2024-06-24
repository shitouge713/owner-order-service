package com.owner.order.service.extend.impl;

import com.owner.order.service.extend.CreateOrderHandler;
import com.owner.order.vo.OrderReqVO;
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
    public boolean canHandle(OrderReqVO vo) {
        return true;
    }

    @Override
    public void beforeToDB(OrderReqVO vo) {
        log.info("RepertoryHandler,库存逻辑");
    }
}
