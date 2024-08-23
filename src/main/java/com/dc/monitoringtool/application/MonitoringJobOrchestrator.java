package com.dc.monitoringtool.application;

import com.dc.monitoringtool.domain.MonitoringResultService;
import com.dc.monitoringtool.domain.MonitoringUrlRequestService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MonitoringJobOrchestrator {

    private final MonitoringUrlRequestService monitoringUrlRequestService;
    private final MonitoringResultService monitoringResultService;

    public void execute(String jobId, String url) {
        var request = monitoringUrlRequestService.request(jobId, url);
        monitoringResultService.saveMonitoringResult(request);
    }

}
