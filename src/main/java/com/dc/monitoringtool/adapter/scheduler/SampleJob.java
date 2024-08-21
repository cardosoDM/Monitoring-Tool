package com.dc.monitoringtool.adapter.scheduler;

import com.dc.monitoringtool.adapter.http.HttpGenericService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
//TODO: Implement the Job interface
@Slf4j
@AllArgsConstructor
public class SampleJob implements Job {
    //todo move this outside
    private final HttpGenericService httpGenericService;

    @Override
    public void execute(JobExecutionContext context) {
        JobDetail jobDetail = context.getJobDetail();
        String url = jobDetail.getJobDataMap().getString(QuartzMapper.URL);
        log.info("Job triggered for url: {}", url);
        httpGenericService.someRestCall(url);
    }
}
