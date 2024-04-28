package com.owner.order.service.order;

import com.owner.order.vo.OrderReqVO;
import com.owner.order.vo.Result;

public interface OrderService {
    Result<String> createOrder(OrderReqVO vo);
}
