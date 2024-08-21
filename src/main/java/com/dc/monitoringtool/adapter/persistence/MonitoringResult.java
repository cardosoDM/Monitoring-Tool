package com.dc.monitoringtool.adapter.persistence;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "monitoringResults")
class MonitoringResult {

    @Id
    private String id;

    @DBRef
    private MonitoringJob job;

    private Status status;

    private long responseTime;

    private LocalDateTime timestamp;

    public enum Status {
        SUCCESS,
        FAILURE
    }
}
