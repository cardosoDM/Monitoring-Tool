package com.dc.monitoringtool.domain;

import com.dc.monitoringtool.domain.model.MonitoringResult;

public interface MonitoringUrlRequestService {
    MonitoringResult request(String jobId, String url);
}
