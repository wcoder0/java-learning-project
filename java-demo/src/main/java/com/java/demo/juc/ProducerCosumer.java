package com.java.demo.juc;

import java.util.concurrent.*;

import org.junit.Test;

public class ProducerCosumer {

    @Test
    public void test() {
        BlockingQueue blockingQueue = new ArrayBlockingQueue(1);

        new Thread() {
            @Override
            public void run() {
                for(int i = 0; i < 10000; i++) {
                    try {
                        blockingQueue.put(new Integer(i));
                        System.out.println("线程1生产--------" + i);
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                try {
                    while(true) {
                        Object take = blockingQueue.take();
                        System.out.println("线程2消费--------" + take);
                    }
                }
                catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    @Test
    public void test2() {
        BlockingQueue blockingQueue = new SynchronousQueue();

        new Thread() {
            @Override
            public void run() {
                for(int i = 0; i < 10000; i++) {
                    try {
                        blockingQueue.put(new Integer(i));
                        System.out.println("线程1生产--------" + i);
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                try {
                    while(true) {
                        Object take = blockingQueue.take();
                        System.out.println("线程2消费--------" + take);

                    }
                }
                catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
