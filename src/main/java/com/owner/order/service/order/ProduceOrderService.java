package com.owner.order.service.order;


/**
 * @Description: 订单业务类
 *
 * @author xub
 * @date 2019/7/12 下午12:57
 */
public interface ProduceOrderService {

     /**
      * @Description: 下单接口
      * @author xub
      */
     boolean save(String transactionId, int userId, int produceId, int total);
}
