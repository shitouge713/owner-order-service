package com.owner.order.service.order.impl;

import com.owner.order.dao.domain.CartDomain;
import com.owner.order.dao.pojo.OCart;
import com.owner.order.service.order.ICartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RefreshScope
public class CartServiceImpl implements ICartService {

    @Autowired
    private CartDomain cartDomain;

    @Value("${flag:true}")
    private Boolean flag;

    @Override
    public Boolean save() {
        OCart cart = new OCart();
        cart.setMerchantCode("22222");
        cart.setUserId(22222L);
        cart.setSkuId("22222");
        cartDomain.save(cart);
        //模拟耗时10s
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        int a = 10;
        int b = 0;
        int c = a / b;
        log.info("订单业务插入数据成功");
        return true;
    }

    @Override
    public void init() {
        while (flag) {
            cartDomain.getById("123");
            System.out.println("123");
        }
    }


}
