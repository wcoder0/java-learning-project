package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 资产目录
 *
 * @Author: Hannibal
 * @Date: 2021/7/1 10:59
 * @Version 1.0
 * @description
 */
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@SpringBootApplication
public class RabbitmqApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitmqApplication.class, args);
        System.out.println("*********RabbitmqApplication 启动成功*********");
    }

}
