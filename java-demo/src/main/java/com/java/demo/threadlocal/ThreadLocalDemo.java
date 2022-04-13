package com.java.demo.threadlocal;

import org.junit.Test;

public class ThreadLocalDemo {

    @Test
    public void test() {
        ThreadLocal threadLocal1 = new ThreadLocal();
        ThreadLocal threadLocal2 = new ThreadLocal();

        threadLocal1.set("1");
        threadLocal2.set("2");
    }
}
