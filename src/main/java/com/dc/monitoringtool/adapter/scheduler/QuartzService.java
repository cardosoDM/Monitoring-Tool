package com.dc.monitoringtool.adapter.scheduler;

import com.dc.monitoringtool.domain.MonitoringJobService;
import com.dc.monitoringtool.domain.model.MonitoringJob;
import lombok.AllArgsConstructor;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class QuartzService implements MonitoringJobService {

    private final Scheduler scheduler;

    @Override
    public MonitoringJob addJob(MonitoringJob job) {
        try {
            UUID uuid = UUID.randomUUID();
            var newJob = job.toBuilder().id(uuid).build();

            var jobDetail = QuartzMapper.fromMonitoringJobToJobDetail(newJob);

            var jobGroupNames = scheduler.getJobGroupNames();
            if (jobGroupNames.size() == 5) {
                //todo: handle exception
                throw new RuntimeException();
            }

            scheduler.addJob(jobDetail, false);
            return newJob;
        } catch (SchedulerException e) {
            //todo: handle exception
            throw new RuntimeException(e);
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
        } catch (SchedulerException e) {
            //todo: Handle exception
            throw new RuntimeException(e);
        }

    }

    private JobDetail getJobDetails(UUID id) {
        try {
            JobKey jobKey = JobKey.jobKey(id.toString());
            if (!scheduler.checkExists(jobKey)) {
                throw new RuntimeException("Job with ID " + id + " does not exist.");
            }
            return scheduler.getJobDetail(jobKey);
        } catch (SchedulerException e) {
            // todo: Handle exception
            throw new RuntimeException(e);
        }
    }

}
