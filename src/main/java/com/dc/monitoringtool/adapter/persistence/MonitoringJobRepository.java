package com.dc.monitoringtool.adapter.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
interface MonitoringJobRepository extends MongoRepository<MonitoringJob, String> {
}