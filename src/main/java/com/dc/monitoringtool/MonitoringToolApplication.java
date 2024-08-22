package com.dc.monitoringtool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class MonitoringToolApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonitoringToolApplication.class, args);
    }

}
