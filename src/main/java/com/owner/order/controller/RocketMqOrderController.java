package com.owner.order.controller;

import com.alibaba.fastjson.JSONObject;
import com.owner.order.config.Jms;
import com.owner.order.mq.TransactionProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


/**
 * @author xub
 * @Description: 订单服务相关接口
 * @date 2019/7/12 下午6:01
 */
@Slf4j
@RestController
@RequestMapping("api/v1/order")
public class RocketMqOrderController {

    @Autowired
    private Jms jms;

    @Autowired
    private TransactionProducer transactionProducer;

    /**
     * 商品下单接口
     * 1. 先发送一个prepared消息到mq
     * 2. 消息发送成功，接着执行本地事务，执行成功发送mq确认消息，执行失败发送mq回滚消息
     * 3. 如果发送了确认消息，那么此时B系统会接收到确认消息，然后执行本地的事务
     * 4. mq会自动定时轮询所有prepared消息回调你的接口，问你，这个消息是不是本地事务处理失败了，所有没发送确认消息？那是继续重试还是回滚？
     * 5. 判断逻辑由自己实现
     *
     * @param userId    用户ID
     * @param productId 商品ID
     * @param total     购买数量
     */
    @RequestMapping("save")
    public Object save(int userId, int productId, int total) throws MQClientException {
        //通过uuid 当key
        String uuid = UUID.randomUUID().toString().replace("_", "");
        //封装消息
        JSONObject msgJson = new JSONObject();
        msgJson.put("productId", productId);
        msgJson.put("total", total);
        String jsonString = msgJson.toJSONString();
        //封装消息实体
        Message message = new Message(jms.getOrderTopic(), null, uuid, jsonString.getBytes());
        //发送消息 用 sendMessageInTransaction  第一个参数可以理解成消费方需要的参数 第二个参数可以理解成消费方不需要 本地事务需要的参数
        SendResult sendResult = transactionProducer.getProducer().sendMessageInTransaction(message, userId);
        log.info("发送结果:{}, sendResult:{}", sendResult.getSendStatus(), sendResult);
        if (SendStatus.SEND_OK == sendResult.getSendStatus()) {
            return "成功";
        }
        return "失败";
    }

}

