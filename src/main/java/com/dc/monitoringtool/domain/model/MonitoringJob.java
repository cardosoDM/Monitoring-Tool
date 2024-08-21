package com.dc.monitoringtool.domain.model;

import lombok.Builder;

import java.util.UUID;

/**
 * Represents a monitoring job.
 * <p>
 *     A monitoring job is a task that is scheduled to run at a certain interval.
 *     It is defined by a URL to monitor
 *     a run interval in seconds
 *     and a number of times to repeat the monitoring task.
 *     The monitoring job is uniquely identified by an ID.
 *     The monitoring job is immutable
 */
@Builder(toBuilder = true)
public record MonitoringJob(UUID id,
                            String url,
                            int intervalInMilliSeconds,
                            int repeatCount,
                            int durationInMilliSeconds) {


}
