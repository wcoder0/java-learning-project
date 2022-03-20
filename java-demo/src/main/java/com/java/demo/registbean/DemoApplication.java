package com.java.demo.registbean;


import java.util.*;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
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

        // 获取beanfactory
        DefaultListableBeanFactory beanFactory =
                (DefaultListableBeanFactory) ((ConfigurableApplicationContext) applicationContext).getBeanFactory();

        // BeanDefinitionBuilder构建bean
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(User.class);
        beanDefinitionBuilder.addPropertyValue("id", 1);
        beanDefinitionBuilder.addPropertyValue("name", "123");
        AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
        beanFactory.registerBeanDefinition("user1", beanDefinition);
        User user = applicationContext.getBean(User.class);

        System.out.println(user);

        // GenericBeanDefinition 构建bean
        GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
        genericBeanDefinition.setBeanClass(User.class);
        MutablePropertyValues mutablePropertyValues = new MutablePropertyValues();
        mutablePropertyValues.add("id", 2);
        mutablePropertyValues.add("name", "22");
        genericBeanDefinition.setPropertyValues(mutablePropertyValues);
        beanFactory.registerBeanDefinition("user2", genericBeanDefinition);

        User user2 = applicationContext.getBean("user2", User.class);

        System.out.println(user2);

        // 动态注入StringRedisTemplate
        GenericBeanDefinition genericBeanDefinition1 = new GenericBeanDefinition();
        genericBeanDefinition1.setBeanClass(StringRedisTemplate.class);

        Set<RedisNode> nodes = new HashSet<>();

        RedisClusterConfiguration configuration = new RedisClusterConfiguration();
        configuration.setClusterNodes(nodes);
        configuration.setMaxRedirects(60);
        //需要密码连接的创建对象方式
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(1000);
        jedisPoolConfig.setMinIdle(100);
        jedisPoolConfig.setMaxTotal(6000);
        jedisPoolConfig.setMaxWaitMillis(20);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setMinEvictableIdleTimeMillis(3000);
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(configuration);
        jedisConnectionFactory.setPoolConfig(jedisPoolConfig);
        jedisConnectionFactory.afterPropertiesSet();
        ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
        constructorArgumentValues.addGenericArgumentValue(jedisConnectionFactory);
        genericBeanDefinition1.setConstructorArgumentValues(constructorArgumentValues);

    }

}
