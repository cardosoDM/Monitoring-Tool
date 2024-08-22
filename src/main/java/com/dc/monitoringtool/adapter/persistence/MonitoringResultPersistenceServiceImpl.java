package com.dc.monitoringtool.adapter.persistence;

import com.dc.monitoringtool.domain.MonitoringResultPersistenceService;
import com.dc.monitoringtool.domain.model.MonitoringResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
class MonitoringResultPersistenceServiceImpl implements MonitoringResultPersistenceService {

    private final MonitoringResultRepository monitoringResultRepository;

    @Override
    public List<MonitoringResult> getFilteredResults(LocalDateTime startTimestamp, LocalDateTime endTimestamp, String jobId, String status) {
        return monitoringResultRepository.findByFilters(startTimestamp, endTimestamp, jobId, status)
                .stream()
                .map(MonitorResultEntityMapper::mapToDomain)
                .toList();
    }

    @Override
    public MonitoringResult saveMonitoringResult(MonitoringResult monitoringResult) {
        var monitoringResultEntity = MonitorResultEntityMapper.mapToEntity(monitoringResult);
        var saved = monitoringResultRepository.save(monitoringResultEntity);
        return MonitorResultEntityMapper.mapToDomain(saved);
    }

}
