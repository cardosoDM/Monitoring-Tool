package com.dc.monitoringtool.architecture.rest.dto;


import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public class MonitoringResult {

    private String id;

    private UUID jobId;

    private MonitoringResult.Status status;

    private long responseTime;

    private LocalDateTime timestamp;

    public enum Status {
        SUCCESS,
        FAILURE
    }

}
