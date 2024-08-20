package com.dc.monitoringtool.domain;

import com.dc.monitoringtool.architecture.rest.dto.MonitoringResult;

import java.util.List;
import java.util.Optional;

public interface MonitoringResultService {
    List<MonitoringResult> getAllResults();

    Optional<MonitoringResult> getResultById(String id);
}
