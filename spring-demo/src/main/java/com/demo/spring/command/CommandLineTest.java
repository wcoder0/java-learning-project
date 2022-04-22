package com.demo.spring.command;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineTest implements CommandLineRunner {

    public void run(String... args) throws Exception {

        System.out.println("CommandLineRunner");

    }
}
