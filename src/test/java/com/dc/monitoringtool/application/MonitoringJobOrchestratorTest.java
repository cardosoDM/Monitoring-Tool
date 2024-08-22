package com.dc.monitoringtool.application;

import com.dc.monitoringtool.domain.MonitoringJobService;
import com.dc.monitoringtool.domain.MonitoringResultPersistenceService;
import com.dc.monitoringtool.domain.MonitoringUrlRequestService;
import com.dc.monitoringtool.domain.exception.MonitoringException;
import com.dc.monitoringtool.domain.model.MonitoringJob;
import com.dc.monitoringtool.domain.model.MonitoringResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MonitoringJobOrchestratorTest {

    @Mock
    private MonitoringJobService monitoringJobService;

    @Mock
    private MonitoringUrlRequestService monitoringUrlRequestService;

    @Mock
    private MonitoringResultPersistenceService monitoringResultPersistenceService;

    private MonitoringJobOrchestrator monitoringJobOrchestrator;

    @BeforeEach
    void setUp() {
        monitoringJobOrchestrator = new MonitoringJobOrchestrator(monitoringJobService, monitoringUrlRequestService, monitoringResultPersistenceService);
    }

    @Test
    void addJob_successful() {
        MonitoringJob job = new MonitoringJob(UUID.randomUUID(), "https://example.com", 1000, 60000, 5);
        when(monitoringJobService.getNumberOfJobs()).thenReturn(4);
        when(monitoringJobService.addJob(job)).thenReturn(job);

        MonitoringJob result = monitoringJobOrchestrator.addJob(job);

        assertEquals(job, result);
        verify(monitoringJobService, times(1)).getNumberOfJobs();
        verify(monitoringJobService, times(1)).addJob(job);
    }

    @Test
    void addJob_maximumJobsReached() {
        MonitoringJob job = new MonitoringJob(UUID.randomUUID(), "https://example.com", 1000, 60000, 5);
        when(monitoringJobService.getNumberOfJobs()).thenReturn(5);

        assertThrows(MonitoringException.class, () -> monitoringJobOrchestrator.addJob(job));
        verify(monitoringJobService, times(1)).getNumberOfJobs();
        verify(monitoringJobService, never()).addJob(any());
    }

    @Test
    void triggerJob_successful() {
        UUID jobId = UUID.randomUUID();

        monitoringJobOrchestrator.triggerJob(jobId);

        verify(monitoringJobService, times(1)).triggerJob(jobId);
    }

    @Test
    void execute_successful() {
        String jobId = "jobId";
        String url = "https://example.com";
        var request = mock(MonitoringResult.class);
        when(monitoringUrlRequestService.request(jobId, url)).thenReturn(request);

        monitoringJobOrchestrator.execute(jobId, url);

        verify(monitoringUrlRequestService, times(1)).request(jobId, url);
        verify(monitoringResultPersistenceService, times(1)).saveMonitoringResult(request);
    }

    @Test
    void deleteJob_successful() {
        UUID jobId = UUID.randomUUID();

        monitoringJobOrchestrator.deleteJob(jobId);

        verify(monitoringJobService, times(1)).deleteJob(jobId);
    }
}