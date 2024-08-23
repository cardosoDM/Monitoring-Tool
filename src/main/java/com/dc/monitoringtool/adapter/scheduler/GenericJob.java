package com.dc.monitoringtool.adapter.scheduler;

import com.dc.monitoringtool.application.MonitoringJobOrchestrator;
import com.dc.monitoringtool.domain.model.HttpRequestConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
class GenericJob implements InterruptableJob {
    private final MonitoringJobOrchestrator monitoringJobOrchestrator;

    @Override
    public void execute(JobExecutionContext context) {
        JobDetail jobDetail = context.getJobDetail();
        Object httpRequestConfigObj = jobDetail.getJobDataMap()
                .get(QuartzMapper.HTTP_REQUEST_CONFIG);

        if (httpRequestConfigObj instanceof HttpRequestConfig httpRequestConfig) {
            log.info("Job triggered for url: {}", httpRequestConfig.url());
            monitoringJobOrchestrator.execute(jobDetail.getKey().getName(), httpRequestConfig);
        }else {
            interrupt(context);
            log.error("Invalid job data map");
        }
    }

    private static void interrupt(JobExecutionContext context) {
        try {
            Scheduler scheduler = context.getScheduler();
            JobDetail jobDetail = context.getJobDetail();
            scheduler.interrupt(jobDetail.getKey());
            scheduler.unscheduleJob(context.getTrigger().getKey());
        } catch (SchedulerException e) {
            //do nothing
            log.error("Unable to interrupt job", e);
        }
    }

    @Override
    public void interrupt() {
        log.info("Job interrupted");
    }
}
