package com.xcly.forever.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 消息生产者
 *
 * @author gdc
 * @version 1.0
 */
@Component
public class MsgProducer {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 由于rabbitTemplate的scope属性设置为ConfigurableBeanFactory.SCOPE_PROTOTYPE，所以不能自动注入
     */
    @Lookup
    public RabbitTemplate getRabbitTemplate() {
        //spring自己会覆盖该方法
        return null;
    }

    /**
     * 发送简单消息
     *
     * @param content    消息内容
     * @param exchange   交换机
     * @param routingKey 路由键
     */
    public void sendMsg(String userId, String content, String exchange, String routingKey) {
        Message message = MessageBuilder.withBody(content.getBytes()).setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN).build();
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        try {
            getRabbitTemplate().convertAndSend(exchange, routingKey, message, correlationId);
        } catch (Exception e) {
            //sendMsg(userId,content,exchange,routingKey);
            logger.error("===>>>发送消息到Mq-failed params:{userId:{} correlationId:{}} reason:{}", userId, correlationId, e);
        }
    }

//    /**
//     * 回调 只确认消息是否正确到达 Exchange
//     */
//    @Override
//    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
//        //logger.info(" 回调id:" + correlationData);
//        if (ack) {
//            //  logger.info("消息成功到达Exchange");
//        } else {
//            //   logger.info("消息到达Exchange失败:" + cause);
//        }
//    }
//
//    /**
//     * 消息没有正确到达队列时触发回调，如果正确到达队列不执行
//     */
//    @Override
//    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
//        logger.error("消息主体 message : " + message);
//        logger.error("消息主体 message : " + replyCode);
//        logger.error("描述：" + replyText);
//        logger.error("消息使用的交换器 exchange : " + exchange);
//        logger.error("消息使用的路由键 routing : " + routingKey);
//        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
//        //如果消息发送到交换机成功到达队列失败 重新发送
//        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_A, RabbitConfig.ROUTING_KEY_A, message, correlationId);
//        logger.info("===>>>>到达队列失败--重新发送成功");
//    }
}