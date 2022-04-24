package com.java.demo.juc;

import java.util.concurrent.*;

public class SemaphoreTest {

    public static void main(String[] args) {
        test();
    }


    public static void test() {
        Semaphore semaphore = new Semaphore(3);


        for(int i = 10; i > 0; i--) {
            new Thread(() -> {
                try {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + "获取资源");
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
                finally {
                    System.out.println(Thread.currentThread().getName() + "释放资源");
                    semaphore.release();
                }
            }, i + "").start();
        }
    }
}
