package com.hannibal.kafkademo.config;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import java.util.Random;

/**
 * @Author: Hannibal
 * @Date: 2020/12/17 17:31
 * @Version 1.0
 * @description
 */
@EnableScheduling
@Component
public class CronTimer {

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    // 发送消息
    @Scheduled(cron = "*/2 * * * * ? ")
    public void sendMessage1() {
        System.out.println("________________________");
        for (int i = 0; i < 5; i++) {
            String msg = "Hello," + new Random().nextInt(100);
            System.out.println("发送消息" + msg);
            kafkaTemplate.send("topic9008981", msg);
        }

    }

}
