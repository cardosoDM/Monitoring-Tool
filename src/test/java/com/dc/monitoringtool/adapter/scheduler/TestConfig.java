package com.dc.monitoringtool.adapter.scheduler;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class TestConfig {

    @Bean
    public GenericJob genericJob() {
        return new TestGenericJob();
    }
}