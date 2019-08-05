package com.xcly.forever.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.UUID;

/**
 * RabbitMq配置类
 *
 * @author gdc
 * @version 1.0
 */
@Configuration
public class RabbitConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;


    public static final String EXCHANGE_A = "my-mq-exchange_A";
    public static final String EXCHANGE_B = "my-mq-exchange_B";


    public static final String QUEUE_A = "QUEUE_A";
    public static final String QUEUE_B = "QUEUE_B";

    public static final String ROUTING_KEY_A = "spring-boot-routingKey_A";
    public static final String ROUTING_KEY_B = "spring-boot-routingKey_B";

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host, port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost("/");
        //当缓存已满时，获取Channel的等待时间，单位为毫秒 否则发送抛出空指针异常
        connectionFactory.setChannelCheckoutTimeout(60000);
        connectionFactory.setConnectionLimit(60000);
        //开启发送确认模式（只能保证正确到达交换机）
        connectionFactory.setPublisherConfirms(true);
        //开启发送确认模式 （和mandatory=true一起使用 保证正确路由到队列）
        connectionFactory.setPublisherReturns(true);
        return connectionFactory;
    }

    /**
     * RabbitTemplate
     * 注意:必须是prototype类型
     *
     * @return
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setReplyTimeout(60000);
        //rabbitTemplate如果为单例的话，那回调就是最后设置的内容
        //当消息不能路由到队列中去的时候，会触发return method 可以做重发处理
        //如果mandatory没有设置，则当消息不能路由到队列的时候，server会删除该消息
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
//            logger.info(" <<<===发送-消息到交换机-回调-成功 回调id:{}", correlationData.getId());
            if (ack) {
//                logger.info("<<<===发送-消息到交换机-成功:回调id:{}", correlationData.getId());
            } else {
                logger.error("<<<===发送-消息到交换机-失败 回调id:{},reason:{}", correlationData.getId(), cause);
                //TODO 如果消息发送到交换机成功到达队列失败 重新发送 因为没有返回具体消息以及路由信息 所以需要自己预先保存关联
            }
        });
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            logger.error("<<<===发送-消息到队列-失败 message:{} replyCode:{} replyText:{} exchange:{} routingKey:{}", message, replyCode, replyText, exchange, routingKey);
            CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
            //TODO 如果消息发送到交换机成功到达队列失败 重新发送
            //rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_A, RabbitConfig.ROUTING_KEY_A, message, correlationId);
            //logger.info("===>>>重发-消息到队列-成功");
        });
        return rabbitTemplate;
    }


    /**
     * 针对消费者配置
     * 1. 设置交换机类型
     * 2. 将队列绑定到交换机
     * FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
     * HeadersExchange ：通过添加属性key-value匹配
     * DirectExchange:按照routingkey分发到指定队列
     * TopicExchange:多关键字匹配
     */
    @Bean
    public DirectExchange defaultExchange() {
        return new DirectExchange(EXCHANGE_A);
    }

    /**
     * 获取队列A
     *
     * @return
     */
    @Bean
    public Queue queueA() {
        //队列持久
        return new Queue(QUEUE_A, true);
    }

    /**
     * 获取队列B
     *
     * @return
     */
    @Bean
    public Queue queueB() {
        //队列持久
        return new Queue(QUEUE_B, true);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queueA()).to(defaultExchange()).with(RabbitConfig.ROUTING_KEY_A);
    }

    @Bean
    public Binding bindingB() {
        return BindingBuilder.bind(queueB()).to(defaultExchange()).with(RabbitConfig.ROUTING_KEY_B);
    }


}