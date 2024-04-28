package com.owner.order.service.extend;

import com.owner.order.vo.OrderReqVO;
import org.springframework.stereotype.Component;

/**
 * 创建订单插件链
 * CreateOrderPluginChain：扩展插件集合链
 * CreateOrderHandler：接口操作类
 *
 * @author sxl
 */
@Component
public class CreateOrderPluginChain extends CommonPluginChain<CreateOrderHandler> {

    public void prepareUploadToDB(OrderReqVO vo) {
        forEachChainConsumer(a -> a.beforeToDB(vo));
    }

    public void afterUploadToDBWithTst() {
        forEachChainConsumer(a -> a.afterToDBWithTst());
    }

    public void afterUploadToDBWithoutTst() {
        forEachChainConsumer(a -> a.afterToDBWithoutTst());
    }
}
