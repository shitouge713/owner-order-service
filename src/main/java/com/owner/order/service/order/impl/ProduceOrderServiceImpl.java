package com.owner.order.service.order.impl;

import com.alibaba.fastjson.JSONObject;
import com.owner.order.client.ProduceClient;
import com.owner.order.service.order.ProduceOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author sxl
 * @Description: 下单实现类
 * @date 2024/5/8 下午2:05
 */
@Slf4j
@Service
public class ProduceOrderServiceImpl implements ProduceOrderService {

    @Autowired
    private ProduceClient produceClient;

    @Override
    public boolean save(String transactionId, int userId, int produceId, int total) {
        //校验：该商品是否存在、库存是否够
        String response = produceClient.findById(produceId);
        JSONObject jsonObject = JSONObject.parseObject(response);
        Integer store = jsonObject.getInteger("store");
        String produceName = jsonObject.getString("produceName");
        if (store == null) {
            log.info("找不到商品消息，商品ID = {}", produceId);
            return false;
        }
        log.info("商品存在,商品ID = {},商品名称:{},当前库存 = {}", produceId, produceName, store);
        // 如果实际库存小于库存
        if (store - total < 0) {
            log.info("库存不足，扣减失败。商品ID = {},商品当前库存 = {},所需库存 = {}", produceId, store, total);
            return false;
        }
        int a = 10, b = 0;
        int c = a / b;
        //业务逻辑,执行下单操作
        log.info("下单成功");
        return true;
    }
}
