package com.owner.order.mqservice;

import com.alibaba.fastjson.JSONObject;
import com.owner.order.config.Jms;
import com.owner.order.service.order.ProduceOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;


/**
 * @author sxl
 * @Description: 分布式事务RocketMQ 生产者
 * @date 2019/7/15 下午11:40
 */
@Slf4j
@Component
public class TransactionProducer {

    /**
     * 需要自定义事务监听器 用于 事务的二次确认 和 事务回查
     */
    private TransactionListener transactionListener;

    /**
     * 这里的生产者和之前的不一样
     */
    private TransactionMQProducer producer;

    /**
     * 官方建议自定义线程 给线程取自定义名称 发现问题更好排查
     */
    private ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("client-transaction-msg-check-thread");
            return thread;
        }

    });

    public TransactionProducer(@Autowired Jms jms, @Autowired ProduceOrderService produceOrderService) {
        transactionListener = new TransactionListenerImpl(produceOrderService);
        // 初始化 事务生产者
        producer = new TransactionMQProducer(jms.getOrderTopic());
        // 添加服务器地址
        producer.setNamesrvAddr(jms.getNameServer());
        // 添加事务监听器
        producer.setTransactionListener(transactionListener);
        // 添加自定义线程池
        producer.setExecutorService(executorService);
        start();
    }

    public TransactionMQProducer getProducer() {
        return this.producer;
    }

    /**
     * 对象在使用之前必须要调用一次，只能初始化一次
     */
    public void start() {
        try {
            this.producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * 一般在应用上下文，使用上下文监听器，进行关闭
     */
    public void shutdown() {
        this.producer.shutdown();
    }
}

/**
 * @author xub
 * @Description: 自定义事务监听器
 * @date 2024/5/08 下午14:20
 * 监听生产者发送消息
 */
@Slf4j
class TransactionListenerImpl implements TransactionListener {

    @Autowired
    private ProduceOrderService produceOrderService;

    public TransactionListenerImpl(ProduceOrderService produceOrderService) {
        this.produceOrderService = produceOrderService;
    }

    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        try {
            String transactionId =  msg.getTransactionId();
            JSONObject jsonObject = JSONObject.parseObject(msg.getBody().toString());
            Integer productId = jsonObject.getInteger("productId");
            Integer total = jsonObject.getInteger("total");
            int userId = Integer.parseInt(arg.toString());
            log.info("本地事务执行,transactionId:{},用户id={},商品ID={},销售库存={}", transactionId, userId, productId, total);
            boolean result = produceOrderService.save(transactionId, userId, productId, total);
            log.info("执行本地事务结果,transactionId:{},result:{}", transactionId, result);
            return result ? LocalTransactionState.COMMIT_MESSAGE : LocalTransactionState.ROLLBACK_MESSAGE;
        } catch (Exception e) {
            return LocalTransactionState.UNKNOW;
        }
    }


    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        /**
         * 因为有种情况就是：上面本地事务执行成功了，但是return LocalTransactionState.COMMIT_MESSAG的时候
         * 服务挂了，那么最终 Brock还未收到消息的二次确定，还是个半消息 ，所以当重新启动的时候还是回调这个回调接口。
         * 如果不先查询上面本地事务的执行情况 直接在执行本地事务，那么就相当于成功执行了两次本地事务了。
         */
        //1、必须根据key先去检查本地事务消息是否完成。
        //2、这里返回要么commit 要么rollback。没有必要在返回 UNKNOW
        String transactionId = msg.getTransactionId();
        // 以事务ID为主键，查询本地事务执行情况
        if (isSuccess(transactionId)) {
            return LocalTransactionState.COMMIT_MESSAGE;
        }
        return LocalTransactionState.ROLLBACK_MESSAGE;
    }

    private boolean isSuccess(String transactionId) {
        // 查询数据库 select from 订单表
        return true;
    }
}
