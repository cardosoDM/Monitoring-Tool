// src/test/java/com/dc/monitoringtool/adapter/persistence/MonitoringResultPersistenceServiceImplTest.java
package com.dc.monitoringtool.adapter.persistence;

import com.dc.monitoringtool.domain.model.MonitoringResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MonitoringResultServiceImplTest {

    @Mock
    private MonitoringResultRepository monitoringResultRepository;

    @InjectMocks
    private MonitoringResultServiceImpl monitoringResultPersistenceService;

    private MonitoringResult monitoringResult;
    private MonitoringResultEntity monitoringResultEntity;

    @BeforeEach
    void setUp() {
        monitoringResult = MonitoringResult.builder()
                .id("1")
                .timestamp(LocalDateTime.now())
                .metadata(Map.of("jobId", "testJobId"))
                .status("SUCCESS")
                .responseTime(100L)
                .build();

        monitoringResultEntity = MonitoringResultEntity.builder()
                .id("1")
                .timestamp(LocalDateTime.now())
                .metadata(Map.of("jobId", "testJobId"))
                .status("SUCCESS")
                .responseTime(100L)
                .build();
    }

    @Test
    void getFilteredResults_successful() {
        LocalDateTime startTimestamp = LocalDateTime.now().minusDays(1);
        LocalDateTime endTimestamp = LocalDateTime.now();
        String jobId = "testJobId";
        String status = "SUCCESS";

        when(monitoringResultRepository.findByFilters(startTimestamp, endTimestamp, jobId, status))
                .thenReturn(List.of(monitoringResultEntity));

        List<MonitoringResult> results = monitoringResultPersistenceService.getFilteredResults(startTimestamp, endTimestamp, jobId, status);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(monitoringResult.id(), results.getFirst().id());
    }

    @Test
    void saveMonitoringResult_successful() {
        when(monitoringResultRepository.save(any(MonitoringResultEntity.class)))
                .thenReturn(monitoringResultEntity);

        MonitoringResult savedResult = monitoringResultPersistenceService.saveMonitoringResult(monitoringResult);

        assertNotNull(savedResult);
        assertEquals(monitoringResult.id(), savedResult.id());
    }

    @Test
    void saveMonitoringResult_exceptionThrown() {
        when(monitoringResultRepository.save(any(MonitoringResultEntity.class)))
                .thenThrow(new RuntimeException("Save failed"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> monitoringResultPersistenceService.saveMonitoringResult(monitoringResult));

        assertEquals("Save failed", exception.getMessage());
    }
}