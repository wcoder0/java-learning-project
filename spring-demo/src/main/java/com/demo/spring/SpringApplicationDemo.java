package com.demo.spring;

import com.demo.spring.createbean.BeanImport;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;


@SpringBootApplication
@Import(BeanImport.class)
public class SpringApplicationDemo {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(SpringApplicationDemo.class, args);
        System.out.println("*********SpringApplicationDemo启动成功*********");

        /**
         * 只能获取beandefinition  不能获取 registsingleton 的bean name
         */
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();

        for(String beanDefinitionName : beanDefinitionNames) {

            Object bean = applicationContext.getBean(beanDefinitionName);

            if(bean.getClass().getPackage().getName().startsWith("com.demo.spring")) {
                System.out.println(beanDefinitionName);
            }
        }
    }
}
