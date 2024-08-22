package com.dc.monitoringtool.adapter.persistence;

import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@AllArgsConstructor
class MonitoringResultRepositoryImpl implements MonitoringResultRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    @Retryable(maxAttempts = 1, backoff = @Backoff(delay = 100))
    public List<MonitoringResultEntity> findByFilters(LocalDateTime startTimestamp, LocalDateTime endTimestamp, String jobId, String status) {
        Query query = new Query();

        if (startTimestamp != null && endTimestamp != null) {
            query.addCriteria(Criteria.where("timestamp").gte(startTimestamp).lte(endTimestamp));
        } else if (startTimestamp != null) {
            query.addCriteria(Criteria.where("timestamp").gte(startTimestamp));
        } else if (endTimestamp != null) {
            query.addCriteria(Criteria.where("timestamp").lte(endTimestamp));
        }

        if (jobId != null && !jobId.isEmpty()) {
            query.addCriteria(Criteria.where("metadata.jobId").is(jobId));
        }

        if (status != null) {
            query.addCriteria(Criteria.where("status").is(status));
        }

        return mongoTemplate.find(query, MonitoringResultEntity.class);
    }
}
