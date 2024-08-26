package com.dc.monitoringtool.adapter.persistence;

import com.dc.monitoringtool.domain.model.MonitoringResult;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MonitorResultEntityMapperTest {

    @Test
    void givenValidEntityWhenMapToDomainThenReturnDomain() {
        MonitoringResultEntity entity = MonitoringResultEntity.builder()
                .id("1")
                .timestamp(LocalDateTime.now())
                .metadata(Map.of("jobId", "testJobId"))
                .status("SUCCESS")
                .responseTime(100L)
                .build();

        MonitoringResult result = MonitorResultEntityMapper.mapToDomain(entity);

        assertNotNull(result);
        assertEquals(entity.getId(), result.id());
        assertEquals(entity.getTimestamp(), result.timestamp());
        assertEquals(entity.getMetadata(), result.metadata());
        assertEquals(entity.getStatus(), result.status());
        assertEquals(entity.getResponseTime(), result.responseTime());
    }

    @Test
    void givenNullEntityWhenMapToDomainThenReturnNull() {
        assertNull(MonitorResultEntityMapper.mapToDomain(null));
    }

    @Test
    void givenValidDomainWhenMapToEntityThenReturnEntity() {
        MonitoringResult domain = MonitoringResult.builder()
                .id("1")
                .timestamp(LocalDateTime.now())
                .metadata(Map.of("jobId", "testJobId"))
                .status("SUCCESS")
                .responseTime(100L)
                .build();

        MonitoringResultEntity entity = MonitorResultEntityMapper.mapToEntity(domain);

        assertNotNull(entity);
        assertEquals(domain.id(), entity.getId());
        assertEquals(domain.timestamp(), entity.getTimestamp());
        assertEquals(domain.metadata(), entity.getMetadata());
        assertEquals(domain.status(), entity.getStatus());
        assertEquals(domain.responseTime(), entity.getResponseTime());
    }

    @Test
    void givenNullDomainWhenMapToEntityThenReturnNull() {
        assertNull(MonitorResultEntityMapper.mapToEntity(null));
    }
}