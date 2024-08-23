package com.dc.monitoringtool.domain.model;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Represents the result of a monitoring task.
 * <p>
 * The monitoring result includes the ID of the monitoring job that was executed,
 * the timestamp when the monitoring task was executed,
 * the metadata of the monitoring task,
 * the status of the monitoring task (SUCCESS or FAILURE),
 * and the response time of the monitoring task in milliseconds.
 * The MonitoringResult is immutable.
 */
@Builder
public record MonitoringResult(String id,
                               LocalDateTime timestamp,
                               Map<String, Object> metadata,
                               String status,
                               long responseTime) {

    public static final String SUCCESS = "SUCCESS";
    public static final String FAILURE = "FAILURE";
}
