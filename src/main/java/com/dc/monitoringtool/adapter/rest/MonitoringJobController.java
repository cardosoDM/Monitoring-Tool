package com.dc.monitoringtool.adapter.rest;

import com.dc.monitoringtool.domain.MonitoringJobService;
import com.dc.monitoringtool.domain.model.MonitoringJob;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@AllArgsConstructor
@RestController
public class MonitoringJobController {

    private final MonitoringJobService monitoringJobService;

    @PostMapping("/jobs")
    public ResponseEntity<MonitoringJob> addJob(@RequestBody MonitoringJob job) {
        return ResponseEntity.ok(monitoringJobService.addJob(job));
    }

    @GetMapping("/jobs/{id}")
    public ResponseEntity<MonitoringJob> getJob(@PathVariable UUID id) {
        return ResponseEntity.ok(monitoringJobService.getJob(id));
    }

    @PutMapping("/jobs/{id}")
    public ResponseEntity<MonitoringJob> updateJob(@PathVariable UUID id, @RequestBody MonitoringJob job) {
        return ResponseEntity.ok(monitoringJobService.updateJob(id, job));
    }

    @PostMapping("/jobs/{id}/delete")
    public ResponseEntity<String> deleteJob(@PathVariable UUID id) {
        monitoringJobService.deleteJob(id);
        return ResponseEntity.ok("Job deleted");
    }

    @PostMapping("/jobs/{id}/trigger")
    public ResponseEntity<String> triggerJob(@PathVariable UUID id) {
        monitoringJobService.triggerJob(id);
        return ResponseEntity.ok("Job triggered");
    }

}
