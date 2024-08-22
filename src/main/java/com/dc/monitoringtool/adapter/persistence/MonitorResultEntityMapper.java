package com.dc.monitoringtool.adapter.persistence;

import com.dc.monitoringtool.domain.model.MonitoringResult;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class MonitorResultEntityMapper {

    public static MonitoringResult mapToDomain(MonitoringResultEntity entity) {
        if (entity == null) {
            return null;
        }

        return MonitoringResult.builder()
                .id(entity.getId())
                .timestamp(entity.getTimestamp())
                .metadata(entity.getMetadata())
                .status(entity.getStatus())
                .responseTime(entity.getResponseTime())
                .build();
    }

    public static MonitoringResultEntity mapToEntity(MonitoringResult domain) {
        if (domain == null) {
            return null;
        }

        return MonitoringResultEntity.builder()
                .id(domain.id())
                .timestamp(domain.timestamp())
                .metadata(domain.metadata())
                .status(domain.status())
                .responseTime(domain.responseTime())
                .build();
    }
}