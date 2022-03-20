package com.java.demo;


import com.java.demo.registbean.User;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import redis.clients.jedis.JedisPoolConfig;

@EnableAsync
@EnableScheduling
@SpringBootApplication
@EnableTransactionManagement
public class DemoApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(DemoApplication.class, args);
        System.out.println("*********DemoApplication启动成功*********");

    }

}
