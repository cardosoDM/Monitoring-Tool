package com.dc.monitoringtool.adapter.persistence;

import com.dc.monitoringtool.AbstractContainerBase;
import com.dc.monitoringtool.MonitoringToolApplication;
import com.mongodb.client.MongoClients;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = MonitoringToolApplication.class)
@ContextConfiguration(classes = MonitoringResultRepositoryImplTest.TestConfig.class)
class MonitoringResultRepositoryImplTest extends AbstractContainerBase {

    @Autowired
    private MongoTemplate mongoTemplate;

    private MonitoringResultRepositoryImpl monitoringResultRepository;

    @BeforeEach
    void setUp() {
        monitoringResultRepository = new MonitoringResultRepositoryImpl(mongoTemplate);
        MonitoringResultEntity monitoringResultEntity = MonitoringResultEntity.builder()
                .id("1")
                .timestamp(LocalDateTime.now())
                .metadata(Map.of("jobId", "testJobId"))
                .status("SUCCESS")
                .responseTime(100L)
                .build();
        mongoTemplate.save(monitoringResultEntity);
    }

    @AfterEach
    void tearDown() {
        mongoTemplate.dropCollection(MonitoringResultEntity.class);
    }

    @Test
    void givenAllFiltersWhenFindByFiltersThenReturnResults() {
        LocalDateTime startTimestamp = LocalDateTime.now().minusDays(1);
        LocalDateTime endTimestamp = LocalDateTime.now();
        String jobId = "testJobId";
        String status = "SUCCESS";

        List<MonitoringResultEntity> results = monitoringResultRepository.findByFilters(startTimestamp, endTimestamp, jobId, status);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("1", results.getFirst().getId());
    }

    @Configuration
    static class TestConfig {
        @Bean
        public MongoTemplate mongoTemplate() {
            return new MongoTemplate(MongoClients.create(AbstractContainerBase.MONGO_DB_CONTAINER.getReplicaSetUrl()), "test");
        }
    }
}