package com.dc.monitoringtool.application;

import com.dc.monitoringtool.domain.MonitoringResultService;
import com.dc.monitoringtool.domain.MonitoringUrlRequestService;
import com.dc.monitoringtool.domain.model.HttpRequestConfig;
import com.dc.monitoringtool.domain.model.MonitoringResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

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
        HttpRequestConfig httpRequestConfig = new HttpRequestConfig("https://example.com", "GET", Collections.emptyMap(), null);
        var request = mock(MonitoringResult.class);
        when(monitoringUrlRequestService.request(jobId, httpRequestConfig)).thenReturn(request);

        monitoringJobOrchestrator.execute(jobId, httpRequestConfig);

        verify(monitoringUrlRequestService, times(1)).request(jobId, httpRequestConfig);
        verify(monitoringResultService, times(1)).saveMonitoringResult(request);
    }
}