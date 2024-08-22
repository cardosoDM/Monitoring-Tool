package com.dc.monitoringtool.adapter.scheduler;

import com.dc.monitoringtool.application.MonitoringJobOrchestrator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenericJobTest {

    @Mock
    private MonitoringJobOrchestrator monitoringJobOrchestrator;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @Mock
    private JobDetail jobDetail;

    private GenericJob genericJob;

    @BeforeEach
    void setUp() {
        genericJob = new GenericJob(monitoringJobOrchestrator);
    }

    @Test
    void execute_successful() {
        JobDataMap jobDataMap = new JobDataMap();
        String url = "https://example.com";
        jobDataMap.put("url", url);
        when(jobExecutionContext.getJobDetail()).thenReturn(jobDetail);
        when(jobDetail.getJobDataMap()).thenReturn(jobDataMap);
        when(jobDetail.getKey()).thenReturn(JobKey.jobKey("testJob"));

        genericJob.execute(jobExecutionContext);

        verify(monitoringJobOrchestrator, times(1)).execute("testJob", url);
    }

    @Test
    void execute_exceptionThrown() {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("url", "http://example.com");
        when(jobExecutionContext.getJobDetail()).thenReturn(jobDetail);
        when(jobDetail.getJobDataMap()).thenReturn(jobDataMap);
        when(jobDetail.getKey()).thenReturn(JobKey.jobKey("testJob"));
        doThrow(new RuntimeException("Execution failed")).when(monitoringJobOrchestrator).execute(anyString(), anyString());

        try {
            genericJob.execute(jobExecutionContext);
        } catch (Exception e) {
            // Exception is expected
        }

        verify(monitoringJobOrchestrator, times(1)).execute("testJob", "http://example.com");
    }
}