package com.java.demo.juc;

import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;

import org.junit.Test;

public class ProducerCosumer4 {

    private volatile Boolean flag = false;

    @Test
    public void test() throws Exception {
        AtomicBoolean pro = new AtomicBoolean(true);

        for (int i = 0; i < 10; i++) {
            ThreadDemo t1 = new ThreadDemo();
            t1.setName("t1");

            ThreadDemo t2 = new ThreadDemo();
            t2.setName("t2");

            t1.setThread(t2);
            t2.setThread(t1);

            t1.start();
            t2.start();
            t1.join();
            t2.join();
        }
    }


    class ThreadDemo extends Thread {
        ThreadDemo thread;

        ThreadDemo() {
            super();
        }

        ThreadDemo(ThreadDemo thread) {
            super();
            this.thread = thread;
        }

        public void setThread(ThreadDemo thread) {
            this.thread = thread;
        }


        @Override
        public void run() {
            while (flag) {
                flag = true;
                LockSupport.park();
            }

            System.out.println(Thread.currentThread().getName() + "执行");
            flag = false;
            LockSupport.unpark(thread);
        }
    }
}
