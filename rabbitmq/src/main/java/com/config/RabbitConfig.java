package com.config;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Hannibal
 * @Date: 2020/10/26 18:19
 * @Version 1.0
 * @description RabbitMQ配置类
 */
@Slf4j
@Configuration
public class RabbitConfig implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    public static final String PROCESS_ENGINE_EXCHANGE = RabbitExchangeEnum.PROCESS_ENGINE_EXCHANGE.exchange();

    public static final String APPLY_QUEUE = RabbitQueueEnum.APPLY_QUEUE.queue();

    public static final String FEEDBACK_QUEUE = RabbitQueueEnum.FEEDBACK_QUEUE.queue();

    @Bean
    public Queue applyQueue() {
        return new Queue("test", true);
    }

    @Bean
    public Queue feedbackQueue() {
        return new Queue(FEEDBACK_QUEUE, true);
    }

    @Bean
    public TopicExchange processEngineExchange() {
        return new TopicExchange(PROCESS_ENGINE_EXCHANGE);
    }

    @Bean
    public Binding bindingExchangeMessage() {
        return BindingBuilder.bind(applyQueue()).to(processEngineExchange()).with(APPLY_QUEUE);
    }

    @Bean
    public Binding bindingExchangeMessage1() {
        return BindingBuilder.bind(feedbackQueue()).to(processEngineExchange()).with(FEEDBACK_QUEUE);
    }

    @Bean
    MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Resource
    RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }

    /**
     * 消息发送回调函数（只对异常消息做记录）
     *
     * @param correlationData
     * @param ack
     * @param cause
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (!ack) {
            log.error("消息唯一标识:" + correlationData);
            log.error("确认结果:" + ack);
            log.error("失败原因:" + cause);
        }
    }

    /**
     * 交换机路由不到队列触发（正常情况下不会触发）
     *
     * @param message
     * @param replyCode
     * @param replyText
     * @param exchanges
     * @param routingKey
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchanges, String routingKey) {
        log.info("消息主题 message:" + message);
        log.info("消息主题 message:" + replyCode);
        log.info("描述:" + replyText);
        log.info("消息使用交换器:" + exchanges);
        log.info("消息使用路由:" + routingKey);
    }
}