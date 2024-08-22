package com.dc.monitoringtool.adapter.scheduler;

import com.dc.monitoringtool.domain.exception.MonitoringException;
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
    void testAddJob_Success() throws SchedulerException {
        // Given
        MonitoringJob job = MonitoringJob.builder().build();
        JobDetail jobDetail = JobBuilder.newJob(genericJob.getClass()).withIdentity("TestJob").build();


        try (var mockedMapper = mockStatic(QuartzMapper.class)) {
            mockedMapper.when(() -> QuartzMapper.fromMonitoringJobToJobDetail(any(MonitoringJob.class), eq(genericJob.getClass())))
                    .thenReturn(jobDetail);

            doNothing().when(scheduler).addJob(any(JobDetail.class), eq(false));
            // When
            MonitoringJob newJob = quartzService.addJob(job);

            // Then
            assertNotNull(newJob.id()); // Ensure UUID is generated
            verify(scheduler).addJob(jobDetail, false);
        }
    }

    @Test
    void testAddJob_Exception() throws SchedulerException {
        // Given
        MonitoringJob job = MonitoringJob.builder().build();
        doThrow(new SchedulerException()).when(scheduler).addJob(any(JobDetail.class), eq(false));

        // When & Then
        assertThrows(MonitoringException.class, () -> quartzService.addJob(job));
    }

    @Test
    void testTriggerJob_Success() throws SchedulerException {
        // Given
        UUID jobId = UUID.randomUUID();
        JobDetail jobDetail = JobBuilder.newJob(genericJob.getClass()).withIdentity(jobId.toString()).build();
        jobDetail.getJobDataMap().put(QuartzMapper.DURATION_IN_MILLI_SECONDS, 10000);
        jobDetail.getJobDataMap().put(QuartzMapper.INTERVAL_IN_MILLI_SECONDS, 1000);
        jobDetail.getJobDataMap().put(QuartzMapper.REPEAT_COUNT, 10);

        when(scheduler.getJobDetail(JobKey.jobKey(jobId.toString()))).thenReturn(jobDetail);
        when(scheduler.checkExists(JobKey.jobKey(jobId.toString()))).thenReturn(true);

        // When
        quartzService.triggerJob(jobId);

        // Then
        verify(scheduler).scheduleJob(any(Trigger.class));
    }

    @Test
    void testTriggerJob_JobNotFound() throws SchedulerException {
        // Given
        UUID jobId = UUID.randomUUID();
        when(scheduler.checkExists(JobKey.jobKey(jobId.toString()))).thenReturn(false);

        // When & Then
        assertThrows(MonitoringException.class, () -> quartzService.triggerJob(jobId));
    }

    @Test
    void testTriggerJob_AlreadyExists_Exception() {
        // Given
        UUID jobId = UUID.randomUUID();

        // When & Then
        assertThrows(MonitoringException.class, () -> quartzService.triggerJob(jobId));
    }

    @Test
    void testTriggerJob_Exception() throws SchedulerException {
        // Given
        UUID jobId = UUID.randomUUID();
        JobDetail jobDetail = JobBuilder.newJob(genericJob.getClass()).withIdentity(jobId.toString()).build();
        jobDetail.getJobDataMap().put(QuartzMapper.DURATION_IN_MILLI_SECONDS, 10000);
        jobDetail.getJobDataMap().put(QuartzMapper.INTERVAL_IN_MILLI_SECONDS, 1000);
        jobDetail.getJobDataMap().put(QuartzMapper.REPEAT_COUNT, 10);

        when(scheduler.getJobDetail(JobKey.jobKey(jobId.toString()))).thenReturn(jobDetail);
        when(scheduler.checkExists(JobKey.jobKey(jobId.toString()))).thenReturn(true);
        doThrow(new SchedulerException()).when(scheduler).scheduleJob(any(Trigger.class));

        //When and Then
        assertThrows(MonitoringException.class, () -> quartzService.triggerJob(jobId));
    }

    @Test
    void testDeleteJob_Success() throws SchedulerException {
        // Given
        UUID jobId = UUID.randomUUID();

        // When
        quartzService.deleteJob(jobId);

        // Then
        verify(scheduler).deleteJob(JobKey.jobKey(jobId.toString()));
    }

    @Test
    void testDeleteJob_Exception() throws SchedulerException {
        // Given
        UUID jobId = UUID.randomUUID();
        doThrow(new SchedulerException()).when(scheduler).deleteJob(JobKey.jobKey(jobId.toString()));

        // When & Then
        assertThrows(MonitoringException.class, () -> quartzService.deleteJob(jobId));
    }

    @Test
    void testGetNumberOfJobs_Success() throws SchedulerException {
        // Given
        when(scheduler.getJobKeys(GroupMatcher.anyGroup())).thenReturn(Set.of(JobKey.jobKey("job1"), JobKey.jobKey("job2")));

        // When
        int numberOfJobs = quartzService.getNumberOfJobs();

        // Then
        assertEquals(2, numberOfJobs);
    }

    @Test
    void testGetNumberOfJobs_Exception() throws SchedulerException {
        // Given
        when(scheduler.getJobKeys(GroupMatcher.anyGroup())).thenThrow(new SchedulerException());

        // When & Then
        assertThrows(MonitoringException.class, () -> quartzService.getNumberOfJobs());
    }

    @Test
    void testGetJobDetails_Success() throws SchedulerException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Given
        UUID jobId = UUID.randomUUID();
        JobDetail jobDetail = JobBuilder.newJob(genericJob.getClass()).withIdentity(jobId.toString()).build();

        when(scheduler.getJobDetail(JobKey.jobKey(jobId.toString()))).thenReturn(jobDetail);
        when(scheduler.checkExists(JobKey.jobKey(jobId.toString()))).thenReturn(true);

        // When
        Method getJobDetailsMethod = QuartzService.class.getDeclaredMethod("getJobDetails", UUID.class);
        getJobDetailsMethod.setAccessible(true);

        JobDetail result = (JobDetail) getJobDetailsMethod.invoke(quartzService, jobId);

        // Then
        assertNotNull(result);
        assertEquals(jobDetail, result);
    }

    @Test
    void testGetJobDetails_JobNotFound() throws SchedulerException, NoSuchMethodException {
        // Given
        UUID jobId = UUID.randomUUID();
        when(scheduler.checkExists(JobKey.jobKey(jobId.toString()))).thenReturn(false);

        Method getJobDetailsMethod = QuartzService.class.getDeclaredMethod("getJobDetails", UUID.class);
        getJobDetailsMethod.setAccessible(true);

        // When & Then
        InvocationTargetException invocationTargetException = assertThrows(InvocationTargetException.class, () -> getJobDetailsMethod.invoke(quartzService, jobId));
        assertInstanceOf(MonitoringException.class, invocationTargetException.getCause());
    }

    @Test
    void testGetJobDetails_DontExists_Exception() throws NoSuchMethodException {
        // Given
        UUID jobId = UUID.randomUUID();

        Method getJobDetailsMethod = QuartzService.class.getDeclaredMethod("getJobDetails", UUID.class);
        getJobDetailsMethod.setAccessible(true);

        // When & Then
        InvocationTargetException invocationTargetException = assertThrows(InvocationTargetException.class, () -> getJobDetailsMethod.invoke(quartzService, jobId));
        assertInstanceOf(MonitoringException.class, invocationTargetException.getCause());
    }

    @Test
    void testGetJobDetails_Exception() throws SchedulerException, NoSuchMethodException {
        // Given
        UUID jobId = UUID.randomUUID();

        when(scheduler.checkExists(JobKey.jobKey(jobId.toString()))).thenReturn(true);
        doThrow(new SchedulerException()).when(scheduler).getJobDetail(any(JobKey.class));

        // When & Then
        Method getJobDetailsMethod = QuartzService.class.getDeclaredMethod("getJobDetails", UUID.class);
        getJobDetailsMethod.setAccessible(true);

        InvocationTargetException invocationTargetException = assertThrows(InvocationTargetException.class, () -> getJobDetailsMethod.invoke(quartzService, jobId));
        assertInstanceOf(MonitoringException.class, invocationTargetException.getCause());
    }
}
