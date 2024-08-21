package com.dc.monitoringtool.adapter.scheduler;

import com.dc.monitoringtool.domain.model.MonitoringJob;
import lombok.NoArgsConstructor;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class QuartzMapper {

    public static final String URL = "url";
    public static final String REPEAT_COUNT = "repeatCount";
    public static final String INTERVAL_IN_MILLI_SECONDS = "intervalInMilliSeconds";
    public static final String DURATION_IN_MILLI_SECONDS = "durationInMilliSeconds";

    public static JobDetail fromMonitoringJobToJobDetail(MonitoringJob job) {
        return JobBuilder.newJob()
                .ofType(SampleJob.class)
                .storeDurably()
                .withIdentity(job.id().toString())
                .usingJobData(URL, job.url())
                .usingJobData(INTERVAL_IN_MILLI_SECONDS, job.intervalInMilliSeconds())
                .usingJobData(DURATION_IN_MILLI_SECONDS, job.durationInMilliSeconds())
                .usingJobData(REPEAT_COUNT, job.repeatCount())
                .build();
    }
}
