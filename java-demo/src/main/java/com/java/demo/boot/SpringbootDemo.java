package com.java.demo.boot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class SpringbootDemo {


    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext("com.java.demo" +
                ".boot.*");
        applicationContext.register(ClassB.class);
        applicationContext.register(ClassA.class);
        System.out.println(applicationContext.getBean(ClassB.class));
        System.out.println(applicationContext.getBean(ClassA.class));
    }
}
