package com.simple.queue;

import java.util.*;
import javax.annotation.Resource;

import com.model.ApplyMsgModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RabbitmqProducer {

    @Resource
    RabbitTemplate rabbitTemplate;


    // 发送消息
    @Scheduled(cron = "0 0/1 * * * ?")
    public void sendMessage1() {
        System.out.println("________________________");
        for(int i = 0; i < 5; i++) {
            String msg = "Hello," + i;
            System.out.println("发送消息" + msg);
            ApplyMsgModel model = new ApplyMsgModel();
            model.setData(msg);
            rabbitTemplate.convertAndSend("test", model);
        }

    }


}
