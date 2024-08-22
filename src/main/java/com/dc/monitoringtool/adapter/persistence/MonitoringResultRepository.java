package com.dc.monitoringtool.adapter.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
interface MonitoringResultRepository extends MongoRepository<MonitoringResultEntity, String>, MonitoringResultRepositoryCustom {
}
