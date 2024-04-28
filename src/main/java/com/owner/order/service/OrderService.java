package com.owner.order.service;

import com.owner.order.vo.OrderReqVO;
import com.owner.order.vo.Result;

public interface OrderService {
    Result<String> createOrder();
}
