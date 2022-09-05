package com.java.demo.juc;

import java.util.concurrent.*;

public class CyclicBarrierTest {


    public static void main(String[] args) {
        test();
    }

    public static void test() {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2, () -> {
            try {
                Thread.sleep(1000);
                System.out.println("所有线程到达公共屏障点，开始一起执行");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        new Thread(() -> {
            try {
                System.out.println("线程1到达公共屏障点");
                cyclicBarrier.await();
                System.out.println("线程1到达公共屏障点，后");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                System.out.println("线程2到达公共屏障点");
                cyclicBarrier.await();
                System.out.println("线程2到达公共屏障点，后");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }).start();

    }
}
