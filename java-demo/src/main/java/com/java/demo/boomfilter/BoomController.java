package com.java.demo.boomfilter;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

/**
 * 同时启动8888和8889,都能拿到session的值
 */
@RestController
@RequestMapping("/boom")
public class BoomController {

    RedissonClient redissonClient;

    @PostConstruct
    public void init() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");
        redissonClient = Redisson.create(config);
    }

    @GetMapping("/get")
    public void getSession() {
        RBloomFilter<Object> master = redissonClient.getBloomFilter("master");
        boolean b = master.tryInit(1000L, 0.0003);

        master.add("test1");
        master.add("test2");
        master.add("test3");

        // true
        System.out.println("master   +++ " + master.contains("test1"));

        master.tryInit(1000L, 0.0003);

        // true
        System.out.println("master   reset " + master.contains("test1"));

        RBloomFilter<Object> slave = redissonClient.getBloomFilter("slave");
        slave.tryInit(1000L, 0.0003);
        slave.add("test1");
        slave.add("test2");
        slave.add("test3");

        // true
        System.out.println("svale   test " + slave.contains("test1"));
    }

}
