package com.java.demo.juc;

import java.util.concurrent.*;

import org.junit.Test;

public class ExchangeTest {

    @Test
    public void test() {

        Exchanger<String> exchanger = new Exchanger();

        new Thread() {
            @Override
            public void run() {
                for(int i = 0; i < 5; i++) {
                    try {
                        exchanger.exchange("-------线程1生产" + i);
                        System.out.println("线程1生产" + i);
                    }
                    catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                for(int i = 0; i < 5; i++) {
                    try {
                        String data = exchanger.exchange(i + "");
                        System.out.println("线程2消费--------" + data);
                    }
                    catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

    }
}
