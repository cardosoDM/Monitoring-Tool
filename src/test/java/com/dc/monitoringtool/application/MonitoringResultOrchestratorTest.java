package com.dc.monitoringtool.application;

import com.dc.monitoringtool.domain.MonitoringResultPersistenceService;
import com.dc.monitoringtool.domain.model.MonitoringResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MonitoringResultOrchestratorTest {

    @Mock
    private MonitoringResultPersistenceService monitoringResultPersistenceService;

    private MonitoringResultOrchestrator monitoringResultOrchestrator;

    @BeforeEach
    void setUp() {
        monitoringResultOrchestrator = new MonitoringResultOrchestrator(monitoringResultPersistenceService);
    }

    @Test
    void getFilteredResults_successful() {
        LocalDateTime startTimestamp = LocalDateTime.now().minusDays(1);
        LocalDateTime endTimestamp = LocalDateTime.now();
        String jobId = "jobId";
        String status = "SUCCESS";

        List<MonitoringResult> expectedResults = List.of(mock(MonitoringResult.class));
        when(monitoringResultPersistenceService.getFilteredResults(startTimestamp, endTimestamp, jobId, status))
                .thenReturn(expectedResults);

        List<MonitoringResult> actualResults = monitoringResultOrchestrator.getFilteredResults(startTimestamp, endTimestamp, jobId, status);

        assertEquals(expectedResults, actualResults);
        verify(monitoringResultPersistenceService, times(1))
                .getFilteredResults(startTimestamp, endTimestamp, jobId, status);
    }
}