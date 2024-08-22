package com.dc.monitoringtool.adapter.rest;

import com.dc.monitoringtool.application.MonitoringJobOrchestrator;
import com.dc.monitoringtool.domain.model.MonitoringJob;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@AllArgsConstructor
@RestController
public class MonitoringJobController {

    private final MonitoringJobOrchestrator monitoringJobOrchestrator;

    @PostMapping("/jobs")
    public ResponseEntity<MonitoringJob> addJob(@RequestBody MonitoringJob job) {
        return ResponseEntity.ok(monitoringJobOrchestrator.addJob(job));
    }

    //delete job
    @PostMapping("/jobs/{id}/delete")
    public ResponseEntity<String> deleteJob(@PathVariable UUID id) {
        monitoringJobOrchestrator.deleteJob(id);
        return ResponseEntity.ok("Job deleted");
    }

    @PostMapping("/jobs/{id}/trigger")
    public ResponseEntity<String> triggerJob(@PathVariable UUID id) {
        monitoringJobOrchestrator.triggerJob(id);
        return ResponseEntity.ok("Job triggered");
    }

}
