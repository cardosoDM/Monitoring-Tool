package com.dc.monitoringtool.adapter.rest;

import com.dc.monitoringtool.application.MonitoringResultOrchestrator;
import com.dc.monitoringtool.domain.model.MonitoringResult;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@RestController
public class MonitoringResultController {

    private final MonitoringResultOrchestrator monitoringResultOrchestrator;

    @GetMapping("/monitoring-results")
    public ResponseEntity<List<MonitoringResult>> getMonitoringResultsByCriteria(
            @RequestParam(value = "startTimestamp") LocalDateTime startTimestamp,
            @RequestParam(value = "endTimestamp") LocalDateTime endTimestamp,
            @RequestParam(value = "jobId", required = false) String jobId,
            @RequestParam(value = "status", required = false) String status) {
        List<MonitoringResult> results = monitoringResultOrchestrator.getFilteredResults(startTimestamp, endTimestamp, jobId, status);
        return ResponseEntity.ok(results);
    }
}