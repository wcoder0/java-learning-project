package com.java.demo.juc;

import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;

import org.junit.Test;

public class ProducerCosumer3 {


    @Test
    public void test() {
        AtomicBoolean pro = new AtomicBoolean(true);
        Object lock = new Object();

        new Thread() {
            @Override
            public void run() {
                for(int i = 0; i < 100; i++) {
                    synchronized(lock) {
                        try {
                            while(!pro.get()) {
                                lock.wait();
                            }

                            System.out.println("线程1生产" + i);
                            pro.set(false);
                            lock.notify();
                        }
                        catch(InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                try {
                    for(int i = 0; i < 100; i++) {
                        synchronized(lock) {
                            try {
                                while(pro.get()) {
                                    lock.wait();
                                }

                                System.out.println("线程2生产" + i);
                                pro.set(true);
                                lock.notify();
                            }
                            catch(InterruptedException e) {
                                e.printStackTrace();
                            }
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
