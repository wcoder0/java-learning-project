package com.java.demo.async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncService {

    @Async
    public void test() throws Exception {
        Thread.sleep(1000);
        System.out.println("async test");
    }
}
