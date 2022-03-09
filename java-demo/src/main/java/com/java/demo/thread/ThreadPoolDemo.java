package com.java.demo.thread;

import java.util.concurrent.*;

import org.junit.Test;

//@SpringBootTest
public class ThreadPoolDemo {

    @Test
    public void test(){
//        ExecutorService executorService = Executors.newFixedThreadPool(1);

        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(2);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 2, 100, TimeUnit.SECONDS, queue);

        threadPoolExecutor.submit(new DemoThread());
        threadPoolExecutor.submit(new DemoThread());
        threadPoolExecutor.submit(new DemoThread());
        threadPoolExecutor.submit(new DemoThread());
        threadPoolExecutor.submit(new DemoThread());
    }


    public class DemoThread implements Runnable{

        @Override
        public void run() {
            System.out.println("执行");
        }
    }

}
