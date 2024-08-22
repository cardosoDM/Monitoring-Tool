package com.dc.monitoringtool.domain;


import com.dc.monitoringtool.domain.model.MonitoringResult;

import java.time.LocalDateTime;
import java.util.List;

public interface MonitoringResultPersistenceService {

    List<MonitoringResult> getFilteredResults(LocalDateTime startTimestamp, LocalDateTime endTimestamp, String jobId, String status);

    MonitoringResult saveMonitoringResult(MonitoringResult monitoringResult);
}
