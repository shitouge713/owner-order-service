package com.owner.order.service.extend.impl;

import com.owner.order.service.extend.CreateOrderHandler;
import com.owner.order.vo.OrderReqVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 消息推送规则
 */
@Component
@Slf4j
@Order(4)
public class MsgPushHandler implements CreateOrderHandler {
    @Override
    public boolean canHandle() {
        return true;
    }

    @Override
    public void beforeToDB(OrderReqVO vo) {
        log.info("MsgPushHandler,推送消息逻辑");
    }
}
