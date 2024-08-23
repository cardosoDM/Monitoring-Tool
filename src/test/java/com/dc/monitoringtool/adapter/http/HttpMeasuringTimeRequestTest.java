package com.dc.monitoringtool.adapter.http;

import com.dc.monitoringtool.domain.model.HttpRequestConfig;
import com.dc.monitoringtool.domain.model.MonitoringResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HttpMeasuringTimeRequestTest {

    @Mock
    private HttpRequestService httpRequestService;

    @InjectMocks
    private HttpMeasuringTimeRequest httpMeasuringTimeRequest;

    private final String jobId = "testJobId";
    private final String url = "https://example.com";
    private final HttpMethod method = HttpMethod.GET;
    private final Object requestBody = null;

    @Test
    void request_successful() {
        HttpRequestConfig httpRequestConfig = new HttpRequestConfig(url, method.name(), Collections.emptyMap(), requestBody);
        when(httpRequestService.executeRequest(method, url, requestBody, Object.class))
                .thenReturn(ResponseEntity.ok("response"));

        MonitoringResult result = httpMeasuringTimeRequest.request(jobId, httpRequestConfig);

        assertNotNull(result);
        assertEquals(MonitoringResult.SUCCESS, result.status());
        assertTrue(result.responseTime() >= 0);
        assertEquals(jobId, result.metadata().get("jobId"));
        assertEquals(httpRequestConfig, result.metadata().get("httpRequestConfig"));
        assertEquals("", result.metadata().get("error"));
    }

    @Test
    void request_invalidJobId() {
        HttpRequestConfig httpRequestConfig = new HttpRequestConfig(url, method.name(), Collections.emptyMap(), requestBody);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> httpMeasuringTimeRequest.request("", httpRequestConfig));

        assertEquals("jobId cannot be null or empty", exception.getMessage());
    }

    @Test
    void request_exceptionThrown() {
        HttpRequestConfig httpRequestConfig = new HttpRequestConfig(url, method.name(), Collections.emptyMap(), requestBody);
        when(httpRequestService.executeRequest(method, url, requestBody, Object.class))
                .thenThrow(new RuntimeException("Request failed"));

        MonitoringResult result = httpMeasuringTimeRequest.request(jobId, httpRequestConfig);

        assertNotNull(result);
        assertEquals(MonitoringResult.FAILURE, result.status());
        assertTrue(result.responseTime() >= 0);
        assertEquals(jobId, result.metadata().get("jobId"));
        assertEquals(httpRequestConfig, result.metadata().get("httpRequestConfig"));
        assertEquals("Request failed", result.metadata().get("error"));
    }

    @Test
    void request_nullUrl() {
        HttpRequestConfig httpRequestConfig = new HttpRequestConfig(null, method.name(), Collections.emptyMap(), requestBody);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> httpMeasuringTimeRequest.request(jobId, httpRequestConfig));

        assertEquals("url cannot be null or empty", exception.getMessage());
    }

    @Test
    void request_emptyUrl() {
        HttpRequestConfig httpRequestConfig = new HttpRequestConfig("", method.name(), Collections.emptyMap(), requestBody);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> httpMeasuringTimeRequest.request(jobId, httpRequestConfig));

        assertEquals("url cannot be null or empty", exception.getMessage());
    }
}