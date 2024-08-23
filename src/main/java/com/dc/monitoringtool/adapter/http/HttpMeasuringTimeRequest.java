package com.dc.monitoringtool.adapter.http;

import com.dc.monitoringtool.domain.MonitoringUrlRequestService;
import com.dc.monitoringtool.domain.model.HttpRequestConfig;
import com.dc.monitoringtool.domain.model.MonitoringResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
class HttpMeasuringTimeRequest implements MonitoringUrlRequestService {
    private final HttpRequestService httpRequestService;

    @Override
    public MonitoringResult request(String jobId, HttpRequestConfig httpRequestConfig) {
        validateParams(jobId, httpRequestConfig);

        var status = MonitoringResult.SUCCESS;
        String error = "";
        long responseTime;

        StopWatch watch = new StopWatch();

        HttpMethod httpMethod = HttpMethod.valueOf(httpRequestConfig.method());

        try {
            log.info("Starting request for jobId: {}", jobId);
            watch.start();
            httpRequestService.executeRequest(
                    httpMethod,
                    httpRequestConfig.url(),
                    httpRequestConfig.body(),
                    Object.class);
        } catch (Exception e) {
            status = MonitoringResult.FAILURE;
            error = e.getMessage();
            log.error("Request failed for jobId: {} with error: {}", jobId, error, e);
        } finally {
            watch.stop();
            responseTime = watch.getTotalTimeMillis();
            log.info("Completed request for jobId: {} in {} ms", jobId, responseTime);
        }

        return MonitoringResult.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .responseTime(responseTime)
                .metadata(Map.of(
                        "jobId", jobId,
                        "httpRequestConfig", httpRequestConfig,
                        "error", error))
                .build();
    }

    private static void validateParams(String jobId, HttpRequestConfig httpRequestConfig) {
        if (jobId == null || jobId.isEmpty()) {
            throw new IllegalArgumentException("jobId cannot be null or empty");
        }

        if (httpRequestConfig == null) {
            throw new IllegalArgumentException("httpRequestConfig cannot be null");
        }

        if (httpRequestConfig.url() == null || httpRequestConfig.url().isEmpty()) {
            throw new IllegalArgumentException("url cannot be null or empty");
        }

        if (httpRequestConfig.method() == null || httpRequestConfig.method().isEmpty()) {
            throw new IllegalArgumentException("method cannot be null or empty");
        }
    }
}