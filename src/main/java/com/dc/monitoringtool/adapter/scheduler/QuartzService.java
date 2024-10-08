package com.dc.monitoringtool.adapter.scheduler;

import com.dc.monitoringtool.domain.MonitoringJobService;
import com.dc.monitoringtool.domain.exception.MonitoringException;
import com.dc.monitoringtool.domain.exception.MonitoringNotFoundException;
import com.dc.monitoringtool.domain.model.MonitoringJob;
import lombok.AllArgsConstructor;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
class QuartzService implements MonitoringJobService {

    private final Scheduler scheduler;

    private final Job genericJob;

    @Override
    public MonitoringJob addJob(MonitoringJob job) {
        try {
            if (getNumberOfJobs() > 5) {
                throw new MonitoringException("Number of jobs exceeded");
            }

            UUID uuid = UUID.randomUUID();
            var newJob = job.toBuilder()
                    .id(uuid)
                    .build();

            var jobDetail = QuartzMapper.fromMonitoringJobToJobDetail(newJob, genericJob.getClass());

            scheduler.addJob(jobDetail, false);
            return newJob;
        } catch (SchedulerException e) {
            throw new MonitoringException("Error when adding Job", e);
        }
    }

    @Override
    public void triggerJob(UUID id) {
        try {
            var jobDetail = getJobDetails(id);

            int durationInSeconds = jobDetail.getJobDataMap()
                    .getInt(QuartzMapper.DURATION_IN_MILLI_SECONDS);

            int intervalInSeconds = jobDetail.getJobDataMap()
                    .getInt(QuartzMapper.INTERVAL_IN_MILLI_SECONDS);

            int repeatCount = jobDetail.getJobDataMap()
                    .getInt(QuartzMapper.REPEAT_COUNT);

            var simpleTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(TriggerKey.triggerKey(UUID.randomUUID().toString()))
                    .forJob(jobDetail.getKey())
                    .startNow()
                    .withSchedule(
                            SimpleScheduleBuilder.simpleSchedule()
                                    .withIntervalInMilliseconds(intervalInSeconds)
                                    .withRepeatCount(repeatCount - 1)
                    )
                    .endAt(DateBuilder.futureDate(durationInSeconds, DateBuilder.IntervalUnit.MILLISECOND))
                    .build();

            scheduler.scheduleJob(simpleTrigger);
        } catch (SchedulerException | MonitoringNotFoundException e ) {
            throw new MonitoringException("Error when triggering Job", e);
        }

    }

    @Override
    public void deleteJob(UUID id) {
        try {
            scheduler.deleteJob(JobKey.jobKey(id.toString()));
        } catch (SchedulerException e) {
            throw new MonitoringException("Error when deleting Job", e);

        }
    }

    @Override
    public int getNumberOfJobs() {
        try {
            return scheduler.getJobKeys(GroupMatcher.anyGroup()).size();
        } catch (SchedulerException e) {
            throw new MonitoringException("Error when getting number of Jobs", e);
        }
    }

    @Override
    public MonitoringJob getJob(UUID id) {
        //get job details
        var jobDetail = getJobDetails(id);
        return QuartzMapper.fromJobDetailToMonitoringJob(jobDetail);
    }

    @Override
    public MonitoringJob updateJob(UUID id, MonitoringJob job) {
        try {
            var jobDetail = QuartzMapper.fromMonitoringJobToJobDetail(job, genericJob.getClass());
            scheduler.addJob(jobDetail, true);
            return job;
        } catch (SchedulerException e) {
            throw new MonitoringException("Error when updating Job", e);
        }
    }

    private JobDetail getJobDetails(UUID id) {
        try {
            JobKey jobKey = JobKey.jobKey(id.toString());
            if (!scheduler.checkExists(jobKey)) {
                throw new MonitoringNotFoundException("Job details dont exist");
            }

            return scheduler.getJobDetail(jobKey);
        } catch (SchedulerException e) {
            throw new MonitoringException("Error when getting Job details", e);
        }
    }

}
