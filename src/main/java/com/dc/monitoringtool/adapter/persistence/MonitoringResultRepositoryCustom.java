package com.dc.monitoringtool.adapter.persistence;

import java.time.LocalDateTime;
import java.util.List;

interface MonitoringResultRepositoryCustom {
    List<MonitoringResultEntity> findByFilters(LocalDateTime startTimestamp, LocalDateTime endTimestamp, String jobId, String status);
}
