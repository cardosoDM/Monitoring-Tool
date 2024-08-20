package com.dc.monitoringtool.domain;

import com.dc.monitoringtool.architecture.rest.dto.MonitoringJob;

public interface MonitoringJobService {
    MonitoringJob addJob(MonitoringJob job);
}
