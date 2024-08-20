package com.dc.monitoringtool.architecture.db.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;

@Data
@Document(collection = "monitoringResults")
public class MonitoringResult {

    @Id
    private String id;

    @DBRef
    private MonitoringJob job;

    private MonitoringResult.Status status;

    private long responseTime;

    private LocalDateTime timestamp;

    public enum Status {
        SUCCESS,
        FAILURE
    }
}
