package com.dc.monitoringtool.adapter.scheduler;

import com.dc.monitoringtool.AbstractContainerBase;
import com.dc.monitoringtool.domain.MonitoringJobService;
import com.dc.monitoringtool.domain.model.HttpRequestConfig;
import com.dc.monitoringtool.domain.model.MonitoringJob;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;

@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Import(TestConfig.class)
class QuartzIntegrationTest extends AbstractContainerBase {

    @Autowired
    private MonitoringJobService monitoringJobService;

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private TestGenericJob genericJob;

    @BeforeEach
    void setUp() throws SchedulerException {
        scheduler.clear();
        mongoTemplate.getDb().drop();
        genericJob.resetLatch();
    }

    @Test
    void addJob_triggerJob_successful() throws InterruptedException {
        HttpRequestConfig httpRequestConfig = new HttpRequestConfig("https://example.com", "GET", Collections.emptyMap(), null);
        MonitoringJob job = new MonitoringJob(UUID.randomUUID(), httpRequestConfig, 1000, 5, 60000);
        MonitoringJob monitoringJob = monitoringJobService.addJob(job);

        monitoringJobService.triggerJob(monitoringJob.id());

        // Wait for the latch to count down
        CountDownLatch latch = genericJob.getLatch();
        boolean await = latch.await(10, TimeUnit.SECONDS);

        assertFalse(await);
    }
}