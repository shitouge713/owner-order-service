package com.owner.order.service.extend;

import org.springframework.stereotype.Component;

/**
 * 创建订单
 *
 * @author sxl
 */
@Component
public class CreateOrderPluginChain extends CommonPluginChain<CreateOrderHandler> {

    public void prepareUploadToDB() {
        forEachChainConsumer(a -> a.beforeToDB());
    }

    public void afterUploadToDBWithTst() {
        forEachChainConsumer(a -> a.afterToDBWithTst());
    }

    public void afterUploadToDBWithoutTst() {
        forEachChainConsumer(a -> a.afterToDBWithoutTst());
    }
}
