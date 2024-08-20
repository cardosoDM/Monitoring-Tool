package com.dc.monitoringtool.architecture.db.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Data
@Document(collection = "monitoringJobs")
public class MonitoringJob {

    @Id
    private String id;

    private String url;

    // Cron expression for scheduling
    private String cronExpression;

    // Constructor to automatically generate UUID for the ID
    public MonitoringJob() {
        this.id = UUID.randomUUID().toString();
    }
}
