package com.dc.monitoringtool.domain.model;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
public record MonitoringResult(String id,
                               LocalDateTime timestamp,
                               Map<String, Object> metadata,
                               String status,
                               long responseTime) {

    public static String SUCCESS = "SUCCESS";
    public static String FAILURE = "FAILURE";
}
