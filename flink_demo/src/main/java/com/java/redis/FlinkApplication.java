package com.java.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAsync
@EnableScheduling
@SpringBootApplication
@EnableTransactionManagement
public class FlinkApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(FlinkApplication.class, args);
        System.out.println("*********DemoApplication启动成功*********");

    }

}
