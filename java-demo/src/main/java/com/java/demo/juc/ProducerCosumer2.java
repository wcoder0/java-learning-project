package com.java.demo.juc;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;

import org.junit.Test;

public class ProducerCosumer2 {


    @Test
    public void test() {
        AtomicBoolean pro = new AtomicBoolean(true);
        ReentrantLock lock = new ReentrantLock();
        Condition condition1 = lock.newCondition();
        Condition condition2 = lock.newCondition();

        new Thread() {
            @Override
            public void run() {
                for(int i = 0; i < 100; i++) {
                    lock.lock();
                    try {
                        while(!pro.get()) {
                            condition1.await();
                        }

                        System.out.println("线程1生产--------" + i);
                        pro.set(false);
                        condition2.signal();
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
                    finally {
                        lock.unlock();
                    }
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                try {
                    for(int i = 0; i < 100; i++) {
                        lock.lock();
                        try {
                            while(pro.get()) {
                                condition2.await();
                            }

                            System.out.println("线程2生产--------" + i);
                            pro.set(true);
                            condition1.signal();
                        }
                        catch(Exception e) {
                            e.printStackTrace();
                        }
                        finally {
                            lock.unlock();
                        }
                    }
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
