package com.dc.monitoringtool.architecture.rest;

import com.dc.monitoringtool.architecture.rest.dto.MonitoringJob;
import com.dc.monitoringtool.domain.MonitoringJobService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class MonitoringJobController {

     private final MonitoringJobService monitoringJobService;

    // add a new monitoring job
     @PostMapping("/jobs")
     public ResponseEntity<MonitoringJob> addJob(@RequestBody MonitoringJob job) {
         return ResponseEntity.ok(monitoringJobService.addJob(job));
     }

}
