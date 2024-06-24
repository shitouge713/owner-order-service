package com.owner.order.service.extend.impl;

import com.owner.order.dao.domain.CartDomain;
import com.owner.order.dao.pojo.OCart;
import com.owner.order.service.extend.CreateOrderHandler;
import com.owner.order.vo.OrderReqVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 促销规则
 */
@Component
@Slf4j
@Order(3)
public class DiscountsHandler implements CreateOrderHandler {
    @Autowired
    private CartDomain cartDomain;
    @Override
    public boolean canHandle(OrderReqVO vo) {
        return true;
    }

    @Override
    public void beforeToDB(OrderReqVO vo) {
        log.info("DiscountsHandler,促销逻辑");
    }

    @Override
    public void afterToDBWithTst() {
        OCart cart = new OCart();
        cart.setMerchantCode("11111");
        cart.setUserId(1111L);
        cart.setSkuId("11111");
        /*int a = 10; int b = 0;
        int c = a/b;*/
        cartDomain.save(cart);
    }
}
