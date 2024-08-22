package com.dc.monitoringtool.adapter.scheduler;

import lombok.Getter;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
@Getter
public class TestGenericJob extends GenericJob {

    private CountDownLatch latch = new CountDownLatch(1);

    public TestGenericJob() {
        super(null);
    }

    @Override
    public void execute(JobExecutionContext context) {
        if (latch != null) {
            latch.countDown();
        }
    }

    public void resetLatch() {
        latch = new CountDownLatch(1);

    }
}