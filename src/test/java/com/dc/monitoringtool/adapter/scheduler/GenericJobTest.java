package com.dc.monitoringtool.adapter.scheduler;

import com.dc.monitoringtool.application.MonitoringJobOrchestrator;
import com.dc.monitoringtool.domain.model.HttpRequestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.*;

import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenericJobTest {

    @Mock
    private MonitoringJobOrchestrator monitoringJobOrchestrator;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @Mock
    private JobDetail jobDetail;

    @Mock
    private Scheduler scheduler;

    private GenericJob genericJob;

    @BeforeEach
    void setUp() {
        genericJob = new GenericJob(monitoringJobOrchestrator);
    }

    @Test
    void execute_successful() {
        JobDataMap jobDataMap = new JobDataMap();
        HttpRequestConfig httpRequestConfig = new HttpRequestConfig("https://example.com", "GET", Collections.emptyMap(), null);
        jobDataMap.put("httpRequestConfig", httpRequestConfig);
        when(jobExecutionContext.getJobDetail()).thenReturn(jobDetail);
        when(jobDetail.getJobDataMap()).thenReturn(jobDataMap);
        when(jobDetail.getKey()).thenReturn(JobKey.jobKey("testJob"));

        genericJob.execute(jobExecutionContext);

        verify(monitoringJobOrchestrator, times(1)).execute("testJob", httpRequestConfig);
    }

    @Test
    void execute_exceptionThrown() {
        JobDataMap jobDataMap = new JobDataMap();
        HttpRequestConfig httpRequestConfig = new HttpRequestConfig("https://example.com", "GET", Collections.emptyMap(), null);
        jobDataMap.put("httpRequestConfig", httpRequestConfig);
        when(jobExecutionContext.getJobDetail()).thenReturn(jobDetail);
        when(jobDetail.getJobDataMap()).thenReturn(jobDataMap);
        when(jobDetail.getKey()).thenReturn(JobKey.jobKey("testJob"));
        doThrow(new RuntimeException("Execution failed")).when(monitoringJobOrchestrator).execute(anyString(), any(HttpRequestConfig.class));

        try {
            genericJob.execute(jobExecutionContext);
        } catch (Exception e) {
            // Exception is expected
        }

        verify(monitoringJobOrchestrator, times(1)).execute("testJob", httpRequestConfig);
    }

    @Test
    void execute_withInvalidJobData_shouldInterrupt() throws SchedulerException {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("invalidKey", "invalidValue");
        when(jobExecutionContext.getJobDetail()).thenReturn(jobDetail);
        when(jobDetail.getJobDataMap()).thenReturn(jobDataMap);
        when(jobExecutionContext.getScheduler()).thenReturn(scheduler);
        when(jobDetail.getKey()).thenReturn(JobKey.jobKey("testJob"));
        when(jobExecutionContext.getTrigger()).thenReturn(mock(Trigger.class));

        genericJob.execute(jobExecutionContext);

        verify(scheduler, times(1)).interrupt(jobDetail.getKey());
        verify(scheduler, times(1)).unscheduleJob(jobExecutionContext.getTrigger().getKey());
    }

    @Test
    void interrupt_successful() {
        genericJob.interrupt();

        verify(monitoringJobOrchestrator, times(0))
                .execute(anyString(), any(HttpRequestConfig.class));
    }
}