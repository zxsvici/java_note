package com.zxs.note.boot.scheduler;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.zxs.note.boot.scheduler.mapper")
public class BootSchedulerApplication {
    public static void main(String[] args) {
        SpringApplication.run(BootSchedulerApplication.class, args);
    }
}
