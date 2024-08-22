package com.dc.monitoringtool.adapter.scheduler;

import com.dc.monitoringtool.domain.model.MonitoringJob;
import org.junit.jupiter.api.Test;
import org.quartz.JobDetail;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class QuartzMapperTest {

    @Test
    void fromMonitoringJobToJobDetail_successful() {
        // Arrange
        UUID jobId = UUID.randomUUID();
        String url = "https://example.com";
        int intervalInMilliSeconds = 1000;
        int durationInMilliSeconds = 60000;
        int repeatCount = 5;

        GenericJob genericJob = mock(GenericJob.class);

        MonitoringJob monitoringJob = new MonitoringJob(jobId, url, intervalInMilliSeconds, repeatCount, durationInMilliSeconds);

        // Act
        JobDetail jobDetail = QuartzMapper.fromMonitoringJobToJobDetail(monitoringJob, genericJob.getClass());

        // Assert
        assertNotNull(jobDetail);
        assertEquals(jobId.toString(), jobDetail.getKey().getName());
        assertEquals(url, jobDetail.getJobDataMap().getString(QuartzMapper.URL));
        assertEquals(intervalInMilliSeconds, jobDetail.getJobDataMap().getInt(QuartzMapper.INTERVAL_IN_MILLI_SECONDS));
        assertEquals(durationInMilliSeconds, jobDetail.getJobDataMap().getInt(QuartzMapper.DURATION_IN_MILLI_SECONDS));
        assertEquals(repeatCount, jobDetail.getJobDataMap().getInt(QuartzMapper.REPEAT_COUNT));
    }
}