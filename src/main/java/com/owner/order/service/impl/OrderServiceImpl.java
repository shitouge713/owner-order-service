package com.owner.order.service.impl;

import com.owner.order.service.CreateOrderPluginChain;
import com.owner.order.service.OrderService;
import com.owner.order.vo.OrderReqVO;
import com.owner.order.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private CreateOrderPluginChain createOrderPluginChain;

    @Override
    public Result<String> createOrder() {
        try {
            log.info("开始执行下单逻辑");
            //插件初始化
            createOrderPluginChain.initPlugin();
            //TODO 业务逻辑
            log.info("执行下单业务逻辑");
            //插入前扩展操作
            createOrderPluginChain.prepareUploadToDB();
            //插入后扩展操作（事务内）
            createOrderPluginChain.afterUploadToDBWithTst();
            //插入后扩展操作（事务外）
            createOrderPluginChain.afterUploadToDBWithoutTst();
        } finally {
            createOrderPluginChain.clearThreadLocal();
        }
        return null;
    }
}
