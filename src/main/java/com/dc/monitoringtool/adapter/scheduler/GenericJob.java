package com.dc.monitoringtool.adapter.scheduler;

import com.dc.monitoringtool.application.MonitoringJobOrchestrator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
class GenericJob implements Job {
    private final MonitoringJobOrchestrator monitoringJobOrchestrator;

    @Override
    public void execute(JobExecutionContext context) {
        JobDetail jobDetail = context.getJobDetail();
        String url = jobDetail.getJobDataMap().getString(QuartzMapper.URL);
        log.info("Job triggered for url: {}", url);
        monitoringJobOrchestrator.execute(jobDetail.getKey().getName(), url);
    }
}
