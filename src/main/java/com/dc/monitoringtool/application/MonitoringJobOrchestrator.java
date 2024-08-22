package com.dc.monitoringtool.application;

import com.dc.monitoringtool.domain.MonitoringJobService;
import com.dc.monitoringtool.domain.MonitoringResultPersistenceService;
import com.dc.monitoringtool.domain.MonitoringUrlRequestService;
import com.dc.monitoringtool.domain.model.MonitoringJob;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class MonitoringJobOrchestrator {

    private final MonitoringJobService monitoringJobService;
    private final MonitoringUrlRequestService monitoringUrlRequestService;
    private final MonitoringResultPersistenceService monitoringResultPersistenceService;

    public MonitoringJob addJob(MonitoringJob job) {
        //todo handle errors
        int numberOfJobs = monitoringJobService.getNumberOfJobs();
        if (numberOfJobs == 5) {
            //todo cannot add more jobs
            throw new RuntimeException();
        }
        return monitoringJobService.addJob(job);
    }

    public void triggerJob(UUID id) {
        //todo handle errors
        monitoringJobService.triggerJob(id);
    }

    public void execute(String jobId, String url) {
        //todo handle errors both requesting and saving
        var request = monitoringUrlRequestService.request(jobId, url);
        monitoringResultPersistenceService.saveMonitoringResult(request);

    }

    public void deleteJob(UUID id) {
        //todo handle errors
        monitoringJobService.deleteJob(id);
    }
}
