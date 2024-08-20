package com.dc.monitoringtool.architecture.db;

import com.dc.monitoringtool.architecture.db.model.MonitoringJob;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitoringJobRepository extends MongoRepository<MonitoringJob, String> {
}