package com.dc.monitoringtool.domain;


import com.dc.monitoringtool.domain.model.MonitoringJob;

import java.util.UUID;

public interface MonitoringJobService {
    MonitoringJob addJob(MonitoringJob job);

    void triggerJob(UUID id);

    void deleteJob(UUID id);

    int getNumberOfJobs();

    MonitoringJob getJob(UUID id);

    MonitoringJob updateJob(UUID id, MonitoringJob job);
}
