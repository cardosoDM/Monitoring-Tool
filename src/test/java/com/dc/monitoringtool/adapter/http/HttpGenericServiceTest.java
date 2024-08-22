// src/test/java/com/dc/monitoringtool/adapter/http/HttpGenericServiceTest.java
package com.dc.monitoringtool.adapter.http;

import com.dc.monitoringtool.domain.model.MonitoringResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HttpGenericServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private HttpGenericService httpGenericService;

    private final String jobId = "testJobId";
    private final String url = "https://example.com";

    @Test
    void request_successful() {
        when(restTemplate.getForObject(url, String.class)).thenReturn("response");

        MonitoringResult result = httpGenericService.request(jobId, url);

        assertNotNull(result);
        assertEquals(MonitoringResult.SUCCESS, result.status());
        assertTrue(result.responseTime() >= 0);
        assertEquals(jobId, result.metadata().get("jobId"));
        assertEquals(url, result.metadata().get("url"));
        assertEquals("", result.metadata().get("error"));
    }

    @Test
    void request_invalidJobId() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> httpGenericService.request("", url));

        assertEquals("jobId cannot be null or empty", exception.getMessage());
    }

    @Test
    void request_exceptionThrown() {
        when(restTemplate.getForObject(url, String.class)).thenThrow(new RuntimeException("Request failed"));

        MonitoringResult result = httpGenericService.request(jobId, url);

        assertNotNull(result);
        assertEquals(MonitoringResult.FAILURE, result.status());
        assertTrue(result.responseTime() >= 0);
        assertEquals(jobId, result.metadata().get("jobId"));
        assertEquals(url, result.metadata().get("url"));
        assertEquals("Request failed", result.metadata().get("error"));
    }

    @Test
    void request_nullUrl() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> httpGenericService.request(jobId, null));

        assertEquals("url cannot be null or empty", exception.getMessage());
    }

    @Test
    void request_emptyUrl() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> httpGenericService.request(jobId, ""));

        assertEquals("url cannot be null or empty", exception.getMessage());
    }
}