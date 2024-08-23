package com.dc.monitoringtool.adapter.persistence;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.TimeSeries;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Document(collection = "monitoringResults")
@TimeSeries(timeField = "timestamp")
@Builder
class MonitoringResultEntity {

    @Id
    private String id;

    private LocalDateTime timestamp;

    private Map<String, Object> metadata;

    private String status;

    private long responseTime;
}
