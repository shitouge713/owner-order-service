package com.owner.order.service.order.impl;

import com.owner.order.dao.domain.CartDomain;
import com.owner.order.dao.pojo.OCart;
import com.owner.order.enums.ReturnStatusEnum;
import com.owner.order.exception.NoWarnException;
import com.owner.order.service.extend.CreateOrderPluginChain;
import com.owner.order.service.order.OrderService;
import com.owner.order.vo.OrderReqVO;
import com.owner.order.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private CartDomain cartDomain;
    /**
     * 拿到扩展插件链
     */
    @Resource
    private CreateOrderPluginChain createOrderPluginChain;

    @Override
    public Result<String> createOrder(OrderReqVO vo) {
        try {
            log.info("开始执行下单逻辑");
            //初始化插件，确认哪些需要执行，通过canHandle判断
            createOrderPluginChain.initPlugin(vo);
            //TODO 业务逻辑
            /*Boolean isLock = redisUtils.getLock(OrderConfig.ORDER_SAVE_KEY + vo.getApplyCode(),
                    "1", 120L);
            if (!isLock) {
                log.warn("开柜记录号已经存在,订单已经在保存中了,不能重复添加订单,applyCode:{}", vo.getApplyCode());
                return Result.failed(ReturnStatusEnum.ORDER_IS_EXIST);
            }*/
            log.info("执行下单业务逻辑");
            //订单保存前扩展操作
            createOrderPluginChain.prepareUploadToDB(vo);
            OCart cart = new OCart();
            cart.setMerchantCode("22222");
            cart.setUserId(22222L);
            cart.setSkuId("22222");
            //事务逻辑
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    try {
                        //保存购物车
                        cartDomain.save(cart);
                        //订单保存后扩展操作（事务内）
                        createOrderPluginChain.afterUploadToDBWithTst();
                    } catch (Exception e) {
                        status.setRollbackOnly();
                        log.error("fatalError,创建订单异常,applyCode:{},vo:{},e:", vo.getApplyCode(), vo, e);
                        throw new NoWarnException(ReturnStatusEnum.DATA_EXCEPTION);
                    }
                }
            });
            //订单保存后扩展操作（事务外）
            createOrderPluginChain.afterUploadToDBWithoutTst();
        } finally {
            createOrderPluginChain.clearThreadLocal();
        }
        return null;
    }
}
