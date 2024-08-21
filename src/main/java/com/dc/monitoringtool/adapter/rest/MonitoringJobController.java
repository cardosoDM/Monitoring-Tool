package com.dc.monitoringtool.adapter.rest;

import com.dc.monitoringtool.domain.MonitoringJobService;
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

    private final MonitoringJobService monitoringJobService;

    // add a new monitoring job
    @PostMapping("/jobs")
    public ResponseEntity<MonitoringJob> addJob(@RequestBody MonitoringJob job) {
        return ResponseEntity.ok(monitoringJobService.addJob(job));
    }

    //trigger a job
    @PostMapping("/jobs/{id}/trigger")
    public ResponseEntity<String> triggerJob(@PathVariable UUID id) {
        monitoringJobService.triggerJob(id);
        return ResponseEntity.ok("Job triggered");
    }

}
