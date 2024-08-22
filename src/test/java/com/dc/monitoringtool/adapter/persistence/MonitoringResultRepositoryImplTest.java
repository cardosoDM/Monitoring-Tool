package com.dc.monitoringtool.adapter.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MonitoringResultRepositoryImplTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private MonitoringResultRepositoryImpl monitoringResultRepository;

    private MonitoringResultEntity monitoringResultEntity;

    @BeforeEach
    void setUp() {
        monitoringResultEntity = MonitoringResultEntity.builder()
                .id("1")
                .timestamp(LocalDateTime.now())
                .metadata(Map.of("jobId", "testJobId"))
                .status("SUCCESS")
                .responseTime(100L)
                .build();
    }

    @Test
    void findByFilters_allFilters() {
        LocalDateTime startTimestamp = LocalDateTime.now().minusDays(1);
        LocalDateTime endTimestamp = LocalDateTime.now();
        String jobId = "testJobId";
        String status = "SUCCESS";

        when(mongoTemplate.find(any(Query.class), eq(MonitoringResultEntity.class)))
                .thenReturn(List.of(monitoringResultEntity));

        List<MonitoringResultEntity> results = monitoringResultRepository.findByFilters(startTimestamp, endTimestamp, jobId, status);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(monitoringResultEntity.getId(), results.getFirst().getId());
    }

    @Test
    void findByFilters_startTimestampOnly() {
        LocalDateTime startTimestamp = LocalDateTime.now().minusDays(1);

        when(mongoTemplate.find(any(Query.class), eq(MonitoringResultEntity.class)))
                .thenReturn(List.of(monitoringResultEntity));

        List<MonitoringResultEntity> results = monitoringResultRepository.findByFilters(startTimestamp, null, null, null);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(monitoringResultEntity.getId(), results.getFirst().getId());
    }

    @Test
    void findByFilters_endTimestampOnly() {
        LocalDateTime endTimestamp = LocalDateTime.now();

        when(mongoTemplate.find(any(Query.class), eq(MonitoringResultEntity.class)))
                .thenReturn(List.of(monitoringResultEntity));

        List<MonitoringResultEntity> results = monitoringResultRepository.findByFilters(null, endTimestamp, null, null);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(monitoringResultEntity.getId(), results.getFirst().getId());
    }

    @Test
    void findByFilters_jobIdAndStatus() {
        String jobId = "testJobId";
        String status = "SUCCESS";

        when(mongoTemplate.find(any(Query.class), eq(MonitoringResultEntity.class)))
                .thenReturn(List.of(monitoringResultEntity));

        List<MonitoringResultEntity> results = monitoringResultRepository.findByFilters(null, null, jobId, status);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(monitoringResultEntity.getId(), results.getFirst().getId());
    }

    @Test
    void findByFilters_noFilters() {
        when(mongoTemplate.find(any(Query.class), eq(MonitoringResultEntity.class)))
                .thenReturn(List.of(monitoringResultEntity));

        List<MonitoringResultEntity> results = monitoringResultRepository.findByFilters(null, null, null, null);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(monitoringResultEntity.getId(), results.getFirst().getId());
    }

    @Test
    void findByFilters_exceptionThrown() {
        when(mongoTemplate.find(any(Query.class), eq(MonitoringResultEntity.class)))
                .thenThrow(new RuntimeException("Query failed"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> monitoringResultRepository.findByFilters(null, null, null, null));

        assertEquals("Query failed", exception.getMessage());
    }
}