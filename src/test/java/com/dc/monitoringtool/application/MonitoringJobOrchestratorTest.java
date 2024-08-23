package com.dc.monitoringtool.application;

import com.dc.monitoringtool.domain.MonitoringResultService;
import com.dc.monitoringtool.domain.MonitoringUrlRequestService;
import com.dc.monitoringtool.domain.model.MonitoringResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MonitoringJobOrchestratorTest {

    @Mock
    private MonitoringUrlRequestService monitoringUrlRequestService;

    @Mock
    private MonitoringResultService monitoringResultService;

    private MonitoringJobOrchestrator monitoringJobOrchestrator;

    @BeforeEach
    void setUp() {
        monitoringJobOrchestrator =
                new MonitoringJobOrchestrator(monitoringUrlRequestService, monitoringResultService);
    }

    @Test
    void execute_successful() {
        String jobId = "jobId";
        String url = "https://example.com";
        var request = mock(MonitoringResult.class);
        when(monitoringUrlRequestService.request(jobId, url)).thenReturn(request);

        monitoringJobOrchestrator.execute(jobId, url);

        verify(monitoringUrlRequestService, times(1)).request(jobId, url);
        verify(monitoringResultService, times(1)).saveMonitoringResult(request);
    }
}