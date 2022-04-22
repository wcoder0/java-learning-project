package com.demo.spring.createbean;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfigutaion {


    @Bean(value = "bean_annotation")
    public MyBean createBean() {

        return new MyBean();
    }


}
