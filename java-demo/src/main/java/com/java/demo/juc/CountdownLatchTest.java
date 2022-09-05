package com.java.demo.juc;

import java.util.concurrent.*;

import org.junit.Test;

public class CountdownLatchTest {

    @Test
    public void test() {

        CountDownLatch countdownLatch1 = new CountDownLatch(1);
        CountDownLatch countdownLatch2 = new CountDownLatch(1);

        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    System.out.println("线程1生产" + i);
                    countdownLatch1.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            try {
                countdownLatch1.await();

                for (int i = 0; i < 10; i++) {
                    try {
                        System.out.println("线程2生产" + i);
                        countdownLatch2.countDown();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();


        new Thread(() -> {
            try {
                countdownLatch2.await();

                for (int i = 0; i < 10; i++) {
                    try {
                        System.out.println("线程3生产" + i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();
    }
}
