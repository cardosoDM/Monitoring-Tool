package com.dc.monitoringtool.adapter.rest;

import com.dc.monitoringtool.domain.MonitoringResultService;
import com.dc.monitoringtool.domain.model.MonitoringResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MonitoringResultController.class)
class MonitoringResultControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MonitoringResultService monitoringResultService;

    private MonitoringResult monitoringResult;

    @BeforeEach
    void setUp() {
        monitoringResult = MonitoringResult.builder()
                .id("1")
                .timestamp(LocalDateTime.now())
                .metadata(Collections.singletonMap("jobId", "testJobId"))
                .status("SUCCESS")
                .responseTime(100L)
                .build();
    }

    @Test
    void getMonitoringResultsByCriteria_allCriteria() throws Exception {
        when(monitoringResultService.getFilteredResults(any(LocalDateTime.class), any(LocalDateTime.class), anyString(), anyString()))
                .thenReturn(List.of(monitoringResult));

        mockMvc.perform(get("/monitoring-results")
                        .param("startTimestamp", LocalDateTime.now().minusDays(1).toString())
                        .param("endTimestamp", LocalDateTime.now().toString())
                        .param("jobId", "testJobId")
                        .param("status", "SUCCESS"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":\"1\",\"timestamp\":\"" + monitoringResult.timestamp() + "\",\"metadata\":{\"jobId\":\"testJobId\"},\"status\":\"SUCCESS\",\"responseTime\":100}]"));
    }

    @Test
    void getMonitoringResultsByCriteria_startTimestampOnly() throws Exception {
        when(monitoringResultService.getFilteredResults(any(LocalDateTime.class), eq(null), eq(null), eq(null)))
                .thenReturn(List.of(monitoringResult));

        mockMvc.perform(get("/monitoring-results")
                        .param("startTimestamp", LocalDateTime.now().minusDays(1).toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getMonitoringResultsByCriteria_endTimestampOnly() throws Exception {
        when(monitoringResultService.getFilteredResults(eq(null), any(LocalDateTime.class), eq(null), eq(null)))
                .thenReturn(List.of(monitoringResult));

        mockMvc.perform(get("/monitoring-results")
                        .param("endTimestamp", LocalDateTime.now().toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getMonitoringResultsByCriteria_jobIdAndStatus() throws Exception {
        when(monitoringResultService.getFilteredResults(eq(null), eq(null), anyString(), anyString()))
                .thenReturn(List.of(monitoringResult));

        mockMvc.perform(get("/monitoring-results")
                        .param("jobId", "testJobId")
                        .param("status", "SUCCESS"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getMonitoringResultsByCriteria_exceptionThrown() throws Exception {
        when(monitoringResultService.getFilteredResults(any(LocalDateTime.class), any(LocalDateTime.class), anyString(), anyString()))
                .thenThrow(new RuntimeException("Query failed"));

        mockMvc.perform(get("/monitoring-results")
                        .param("startTimestamp", LocalDateTime.now().minusDays(1).toString())
                        .param("endTimestamp", LocalDateTime.now().toString())
                        .param("jobId", "testJobId")
                        .param("status", "SUCCESS"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Query failed"));
    }
}