package com.java.demo.juc;

import java.util.*;
import java.util.concurrent.*;

public class SemaphoreTest {

    public static void main(String[] args) {
        //        test();
        test2();
    }

    public static void test2() {
        Semaphore semaphore = new Semaphore(10);

        //模拟100辆车进入停车场
        for(int i = 0; i < 100; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("====" + Thread.currentThread().getName() + "来到停车场");
                        if(semaphore.availablePermits() == 0) {
                            System.out.println("车位不足，请耐心等待");
                        }
                        semaphore.acquire();//获取令牌尝试进入停车场
                        System.out.println(Thread.currentThread().getName() + "成功进入停车场");
                        Thread.sleep(new Random().nextInt(10000));//模拟车辆在停车场停留的时间
                        System.out.println(Thread.currentThread().getName() + "驶出停车场");
                        semaphore.release();//释放令牌，腾出停车场车位
                    }
                    catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, i + "号车");
            thread.start();

        }
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
