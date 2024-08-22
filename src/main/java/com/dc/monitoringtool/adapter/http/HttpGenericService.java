package com.dc.monitoringtool.adapter.http;

import com.dc.monitoringtool.domain.MonitoringUrlRequestService;
import com.dc.monitoringtool.domain.model.MonitoringResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
class HttpGenericService implements MonitoringUrlRequestService {
    private final RestTemplate restTemplate;

    @Override
    public MonitoringResult request(String jobId, String url) {
        if (jobId == null || jobId.isEmpty()) {
            throw new IllegalArgumentException("jobId cannot be null or empty");
        }

        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("url cannot be null or empty");
        }

        var status = MonitoringResult.SUCCESS;
        String error = "";

        StopWatch watch = new StopWatch();

        try {
            executeRequest(url, watch);
        } catch (Exception e) {
            status = MonitoringResult.FAILURE;
            error = e.getMessage();
        }

        return MonitoringResult.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .responseTime(watch.getTotalTimeMillis())
                .metadata(Map.of("jobId", jobId, "url", url, "error", error))
                .build();
    }

    private void executeRequest(String url, StopWatch watch) {
        try {
            watch.start();
            restTemplate.getForObject(url, String.class);
        } finally {
            watch.stop();
        }
    }
}
