package com.owner.order.service;

/**
 * 下单操作扩展接口
 * 类似spring启动时的processor扩展接口
 * 下单过程中，有一些扩展操作，比如算价、库存、优惠、免密扣、消息推送
 * 接口方法定义为default，意味着实现类可以不重写该方法，不重写则调用该接口的默认实现
 */
public interface CreateOrderHandler extends OperatorHandler {

    /**
     * 插入数据库前扩展操作
     */
    default void beforeToDB() {
    }

    /**
     * 插入数据库后扩展操作，在事务中
     */
    default void afterToDBWithTst() {
    }

    /**
     * 插入数据库后扩展操作，在事务外
     */
    default void afterToDBWithoutTst() {
    }

}
