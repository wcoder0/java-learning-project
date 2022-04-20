package com.java.demo.async;

import java.util.concurrent.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
//@SpringBootApplication
@Configuration
@ComponentScan(basePackages = "com.java.demo.async")
public class AsyncApplication {

   @Bean
   public TaskExecutor getTaskeExecuter() {
      ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
      threadPoolTaskExecutor.setCorePoolSize(Runtime.getRuntime().availableProcessors());

      new ThreadPoolExecutor(1, 2, 2000, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));
      Executors.newCachedThreadPool();
      return threadPoolTaskExecutor;
   }


   public static void main(String[] args) throws Exception {

      //      ConfigurableApplicationContext run = SpringApplication.run(AsyncApplication.class, args);

      AnnotationConfigApplicationContext springcontext = new AnnotationConfigApplicationContext(AsyncApplication.class);
      AsyncService bean = springcontext.getBean(AsyncService.class);
      bean.test();
      System.out.println("after");
      //      springcontext.close();
   }

}
