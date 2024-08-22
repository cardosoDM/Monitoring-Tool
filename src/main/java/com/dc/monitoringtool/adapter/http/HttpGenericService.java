package com.dc.monitoringtool.adapter.http;

import com.dc.monitoringtool.domain.MonitoringUrlRequestService;
import com.dc.monitoringtool.domain.model.MonitoringResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;

@Service
@AllArgsConstructor
@Slf4j
class HttpGenericService implements MonitoringUrlRequestService {
    private final RestTemplate restTemplate;

    @Override
    public MonitoringResult request(String jobId, String url) {

        StopWatch watch = new StopWatch();
        var status = MonitoringResult.SUCCESS;
        String error = null;
        try {
            watch.start();
            restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            status = MonitoringResult.FAILURE;
            error = e.getMessage();
        } finally {
            watch.stop();
        }
        long totalTimeMillis = watch.getTotalTimeMillis();

        HashMap<String, String> metadata = new HashMap<>();
        metadata.put("jobId", jobId);
        metadata.put("url", url);
        metadata.put("error", error);

        return MonitoringResult.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .responseTime(totalTimeMillis)
                .metadata(metadata)
                .build();
    }
}
