package com.dc.monitoringtool.adapter.scheduler;

import com.dc.monitoringtool.domain.exception.MonitoringException;
import com.dc.monitoringtool.domain.exception.MonitoringNotFoundException;
import com.dc.monitoringtool.domain.model.HttpRequestConfig;
import com.dc.monitoringtool.domain.model.MonitoringJob;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuartzServiceTest {

    @Mock
    private Scheduler scheduler;

    @Mock
    private Job genericJob;

    @InjectMocks
    private QuartzService quartzService;

    @Test
    void givenMonitoringJobWhenAddJobThenSuccessful() throws SchedulerException {
        
        MonitoringJob job = MonitoringJob.builder().build();
        JobDetail jobDetail = JobBuilder.newJob(genericJob.getClass()).withIdentity("TestJob").build();

        try (var mockedMapper = mockStatic(QuartzMapper.class)) {
            mockedMapper.when(() -> QuartzMapper.fromMonitoringJobToJobDetail(any(MonitoringJob.class), eq(genericJob.getClass())))
                    .thenReturn(jobDetail);

            doNothing().when(scheduler).addJob(any(JobDetail.class), eq(false));
            
            MonitoringJob newJob = quartzService.addJob(job);

            
            assertNotNull(newJob.id()); // Ensure UUID is generated
            verify(scheduler).addJob(jobDetail, false);
        }
    }

    @Test
    void givenSchedulerExceptionWhenAddJobThenThrowMonitoringException() throws SchedulerException {
        
        MonitoringJob job = MonitoringJob.builder().build();
        doThrow(new SchedulerException()).when(scheduler).addJob(any(JobDetail.class), eq(false));

        
        assertThrows(MonitoringException.class, () -> quartzService.addJob(job));
    }

    @Test
    void givenJobIdWhenTriggerJobThenSuccessful() throws SchedulerException {
        
        UUID jobId = UUID.randomUUID();
        JobDetail jobDetail = JobBuilder.newJob(genericJob.getClass()).withIdentity(jobId.toString()).build();
        jobDetail.getJobDataMap().put(QuartzMapper.DURATION_IN_MILLI_SECONDS, 10000);
        jobDetail.getJobDataMap().put(QuartzMapper.INTERVAL_IN_MILLI_SECONDS, 1000);
        jobDetail.getJobDataMap().put(QuartzMapper.REPEAT_COUNT, 10);

        when(scheduler.getJobDetail(JobKey.jobKey(jobId.toString()))).thenReturn(jobDetail);
        when(scheduler.checkExists(JobKey.jobKey(jobId.toString()))).thenReturn(true);

        
        quartzService.triggerJob(jobId);

        
        verify(scheduler).scheduleJob(any(Trigger.class));
    }

    @Test
    void givenJobIdWhenTriggerJobThenThrowMonitoringException() throws SchedulerException {
        
        UUID jobId = UUID.randomUUID();
        when(scheduler.checkExists(JobKey.jobKey(jobId.toString()))).thenReturn(false);

        
        assertThrows(MonitoringException.class, () -> quartzService.triggerJob(jobId));
    }

    @Test
    void givenSchedulerExceptionWhenTriggerJobThenThrowMonitoringException() throws SchedulerException {
        
        UUID jobId = UUID.randomUUID();
        JobDetail jobDetail = JobBuilder.newJob(genericJob.getClass()).withIdentity(jobId.toString()).build();
        jobDetail.getJobDataMap().put(QuartzMapper.DURATION_IN_MILLI_SECONDS, 10000);
        jobDetail.getJobDataMap().put(QuartzMapper.INTERVAL_IN_MILLI_SECONDS, 1000);
        jobDetail.getJobDataMap().put(QuartzMapper.REPEAT_COUNT, 10);

        when(scheduler.getJobDetail(JobKey.jobKey(jobId.toString()))).thenReturn(jobDetail);
        when(scheduler.checkExists(JobKey.jobKey(jobId.toString()))).thenReturn(true);
        doThrow(new SchedulerException()).when(scheduler).scheduleJob(any(Trigger.class));

        
        assertThrows(MonitoringException.class, () -> quartzService.triggerJob(jobId));
    }

    @Test
    void givenJobIdWhenDeleteJobThenSuccessful() throws SchedulerException {
        
        UUID jobId = UUID.randomUUID();

        
        quartzService.deleteJob(jobId);

        
        verify(scheduler).deleteJob(JobKey.jobKey(jobId.toString()));
    }

    @Test
    void givenSchedulerExceptionWhenDeleteJobThenThrowMonitoringException() throws SchedulerException {
        
        UUID jobId = UUID.randomUUID();
        doThrow(new SchedulerException()).when(scheduler).deleteJob(JobKey.jobKey(jobId.toString()));

        
        assertThrows(MonitoringException.class, () -> quartzService.deleteJob(jobId));
    }

    @Test
    void givenSchedulerWhenGetNumberOfJobsThenReturnCount() throws SchedulerException {
        
        when(scheduler.getJobKeys(GroupMatcher.anyGroup())).thenReturn(Set.of(JobKey.jobKey("job1"), JobKey.jobKey("job2")));

        
        int numberOfJobs = quartzService.getNumberOfJobs();

        
        assertEquals(2, numberOfJobs);
    }

    @Test
    void givenSchedulerExceptionWhenGetNumberOfJobsThenThrowMonitoringException() throws SchedulerException {
        
        when(scheduler.getJobKeys(GroupMatcher.anyGroup())).thenThrow(new SchedulerException());

        
        assertThrows(MonitoringException.class, () -> quartzService.getNumberOfJobs());
    }

    @Test
    void givenJobIdWhenGetJobDetailsThenReturnJobDetail() throws SchedulerException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        
        UUID jobId = UUID.randomUUID();
        JobDetail jobDetail = JobBuilder.newJob(genericJob.getClass()).withIdentity(jobId.toString()).build();

        when(scheduler.getJobDetail(JobKey.jobKey(jobId.toString()))).thenReturn(jobDetail);
        when(scheduler.checkExists(JobKey.jobKey(jobId.toString()))).thenReturn(true);

        
        Method getJobDetailsMethod = QuartzService.class.getDeclaredMethod("getJobDetails", UUID.class);
        getJobDetailsMethod.setAccessible(true);

        JobDetail result = (JobDetail) getJobDetailsMethod.invoke(quartzService, jobId);

        
        assertNotNull(result);
        assertEquals(jobDetail, result);
    }

    @Test
    void givenJobIdWhenGetJobDetailsThenThrowMonitoringNotFoundException() throws SchedulerException, NoSuchMethodException {
        
        UUID jobId = UUID.randomUUID();
        when(scheduler.checkExists(JobKey.jobKey(jobId.toString()))).thenReturn(false);

        Method getJobDetailsMethod = QuartzService.class.getDeclaredMethod("getJobDetails", UUID.class);
        getJobDetailsMethod.setAccessible(true);

        
        InvocationTargetException invocationTargetException = assertThrows(InvocationTargetException.class, () -> getJobDetailsMethod.invoke(quartzService, jobId));
        assertInstanceOf(MonitoringNotFoundException.class, invocationTargetException.getCause());
    }

    @Test
    void givenSchedulerExceptionWhenGetJobDetailsThenThrowMonitoringException() throws SchedulerException, NoSuchMethodException {
        
        UUID jobId = UUID.randomUUID();

        when(scheduler.checkExists(JobKey.jobKey(jobId.toString()))).thenReturn(true);
        doThrow(new SchedulerException()).when(scheduler).getJobDetail(any(JobKey.class));

        
        Method getJobDetailsMethod = QuartzService.class.getDeclaredMethod("getJobDetails", UUID.class);
        getJobDetailsMethod.setAccessible(true);

        InvocationTargetException invocationTargetException = assertThrows(InvocationTargetException.class, () -> getJobDetailsMethod.invoke(quartzService, jobId));
        assertInstanceOf(MonitoringException.class, invocationTargetException.getCause());
    }

    @Test
    void givenJobIdWhenGetJobThenReturnMonitoringJob() throws SchedulerException {
        
        UUID jobId = UUID.randomUUID();
        JobDetail jobDetail = JobBuilder.newJob(genericJob.getClass()).withIdentity(jobId.toString()).build();
        jobDetail.getJobDataMap().put("httpRequestConfig", new HttpRequestConfig("https://test.com", "GET", Collections.emptyMap(), null));
        jobDetail.getJobDataMap().put("intervalInMilliSeconds", 1000);
        jobDetail.getJobDataMap().put("durationInMilliSeconds", 10000);
        jobDetail.getJobDataMap().put("repeatCount", 5);

        when(scheduler.getJobDetail(JobKey.jobKey(jobId.toString()))).thenReturn(jobDetail);
        when(scheduler.checkExists(JobKey.jobKey(jobId.toString()))).thenReturn(true);

        
        MonitoringJob result = quartzService.getJob(jobId);

        
        assertNotNull(result);
        assertEquals(jobId, result.id());
        assertEquals("https://test.com", result.httpRequestConfig().url());
        assertEquals("GET", result.httpRequestConfig().method());
        assertEquals(1000, result.intervalInMilliSeconds());
        assertEquals(10000, result.durationInMilliSeconds());
        assertEquals(5, result.repeatCount());
    }

    @Test
    void givenJobIdWhenGetJobThenThrowMonitoringNotFoundException() throws SchedulerException {
        
        UUID jobId = UUID.randomUUID();
        when(scheduler.checkExists(JobKey.jobKey(jobId.toString()))).thenReturn(false);

        
        assertThrows(MonitoringNotFoundException.class, () -> quartzService.getJob(jobId));
    }

    @Test
    void givenSchedulerExceptionWhenGetJobThenThrowMonitoringException() throws SchedulerException {
        
        UUID jobId = UUID.randomUUID();

        when(scheduler.checkExists(JobKey.jobKey(jobId.toString()))).thenReturn(true);
        doThrow(new SchedulerException()).when(scheduler).getJobDetail(any(JobKey.class));

        
        assertThrows(MonitoringException.class, () -> quartzService.getJob(jobId));
    }
}