package com.dc.monitoringtool.adapter.scheduler;

import com.dc.monitoringtool.domain.model.HttpRequestConfig;
import com.dc.monitoringtool.domain.model.MonitoringJob;
import lombok.NoArgsConstructor;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;

import java.util.UUID;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
class QuartzMapper {

    public static final String HTTP_REQUEST_CONFIG = "httpRequestConfig";
    public static final String REPEAT_COUNT = "repeatCount";
    public static final String INTERVAL_IN_MILLI_SECONDS = "intervalInMilliSeconds";
    public static final String DURATION_IN_MILLI_SECONDS = "durationInMilliSeconds";

    public static JobDetail fromMonitoringJobToJobDetail(MonitoringJob job, Class<? extends Job> aClass) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(HTTP_REQUEST_CONFIG, job.httpRequestConfig());
        return JobBuilder.newJob()
                .ofType(aClass)
                .storeDurably()
                .withIdentity(job.id().toString())
                .usingJobData(jobDataMap)
                .usingJobData(INTERVAL_IN_MILLI_SECONDS, job.intervalInMilliSeconds())
                .usingJobData(DURATION_IN_MILLI_SECONDS, job.durationInMilliSeconds())
                .usingJobData(REPEAT_COUNT, job.repeatCount())
                .build();
    }

    public static MonitoringJob fromJobDetailToMonitoringJob(JobDetail jobDetail) {
        return MonitoringJob.builder()
                .id(UUID.fromString(jobDetail.getKey().getName()))
                .httpRequestConfig((HttpRequestConfig) jobDetail.getJobDataMap().get(HTTP_REQUEST_CONFIG))
                .intervalInMilliSeconds(jobDetail.getJobDataMap().getInt(INTERVAL_IN_MILLI_SECONDS))
                .durationInMilliSeconds(jobDetail.getJobDataMap().getInt(DURATION_IN_MILLI_SECONDS))
                .repeatCount(jobDetail.getJobDataMap().getInt(REPEAT_COUNT))
                .build();
    }
}
