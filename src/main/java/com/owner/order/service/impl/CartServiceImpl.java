package com.owner.order.service.impl;

import com.owner.order.dao.domain.CartDomain;
import com.owner.order.dao.pojo.OCart;
import com.owner.order.service.ICartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    private CartDomain cartDomain;

    @Override
    public Boolean save() {
        OCart cart = new OCart();
        cart.setMerchantCode("22222");
        cart.setUserId(22222L);
        cart.setSkuId("22222");
        cartDomain.save(cart);
        return true;
    }


}
