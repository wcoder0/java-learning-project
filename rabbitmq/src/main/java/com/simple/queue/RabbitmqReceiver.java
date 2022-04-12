package com.simple.queue;

import java.io.IOException;

import com.model.ApplyMsgModel;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Author: Hannibal
 * @Date: 2020/10/27 15:35
 * @Version 1.0
 * @description 接收申请消息
 */
@Slf4j
@Component
@RabbitListener(queues = "test")
public class RabbitmqReceiver {


    @RabbitHandler
    public void process(ApplyMsgModel applyMsgModel, Channel channel, Message message) throws IOException {
        log.info("RabbitmqReceiver 消费消息:" + applyMsgModel.getData());

    }
}
