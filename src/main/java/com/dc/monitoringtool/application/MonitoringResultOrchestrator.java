package com.dc.monitoringtool.application;

import com.dc.monitoringtool.domain.MonitoringResultPersistenceService;
import com.dc.monitoringtool.domain.model.MonitoringResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class MonitoringResultOrchestrator {

    private final MonitoringResultPersistenceService monitoringResultPersistenceService;


    public List<MonitoringResult> getFilteredResults(LocalDateTime startTimestamp,
                                                     LocalDateTime endTimestamp,
                                                     String jobId,
                                                     String status) {
        return monitoringResultPersistenceService.getFilteredResults(startTimestamp, endTimestamp, jobId, status);
    }
}
