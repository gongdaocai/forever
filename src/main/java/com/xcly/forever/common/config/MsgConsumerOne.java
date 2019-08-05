//package com.xcly.forever.common.config;
//
//import com.rabbitmq.client.Channel;
//import com.xcly.forever.service.QinagGouService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.annotation.RabbitHandler;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
///**
// * 消费者One
// *
// * @author gdc
// * @version 1.0
// */
//@Component
//@RabbitListener(queues = RabbitConfig.QUEUE_A)
//public class MsgConsumerOne {
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//    @Autowired
//    QinagGouService qinagGouService;
//
//    @RabbitHandler
//    public void process(String msg, Channel channel, Message message) {
//
//        logger.info("===>>>消费者one-接收" + RabbitConfig.QUEUE_A + "消息" + msg);
//        try {
//            qinagGouService.insetOrder(msg);
//            //如果消息处理逻辑成功 则channel手动确认 mq将该消失从队列中移除
//            //如果出现异常:1直接抛出异常
//            // 框架容器，是否开启手动ack按照框架配置
//            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//            logger.info("<<<===消费者one-手动ACK-success");
//        } catch (Exception e) {
//            logger.error("<<<<消费者one-消费消息-失败 msg:{} message:{} reason{}", msg, message, e);
//            //丢弃这条消息 参数3表示是否手动ack失败是否重发
//            // TODO 重发两次发送到死信队列 发送到其他队列
//            try {
//                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
//            } catch (IOException e1) {
//                logger.error("<<<<消费者one-手动NACK-失败 msg:{} message:{} reason{}", msg, message, e1);
//            }
//        }
//    }
//}