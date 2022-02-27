package com.java.daily;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAsync
@EnableScheduling
@SpringBootApplication
@EnableTransactionManagement
public class MedicalEquipmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedicalEquipmentApplication.class, args);
        System.out.println("*********MedicalEquipmentApplication启动成功*********");
    }

}
