package com.dc.monitoringtool.adapter.rest;

import com.dc.monitoringtool.domain.MonitoringJobService;
import com.dc.monitoringtool.domain.exception.MonitoringNotFoundException;
import com.dc.monitoringtool.domain.model.HttpRequestConfig;
import com.dc.monitoringtool.domain.model.MonitoringJob;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MonitoringJobController.class)
class MonitoringJobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MonitoringJobService monitoringJobService;

    private UUID jobId;

    @BeforeEach
    void setUp() {
        jobId = UUID.randomUUID();
    }

    @Test
    void givenValidJobWhenAddJobThenReturnSuccessful() throws Exception {
        HttpRequestConfig httpRequestConfig = new HttpRequestConfig("https://test.com", "GET", Collections.emptyMap(), null);
        final MonitoringJob monitoringJob = MonitoringJob.builder()
                .id(jobId)
                .httpRequestConfig(httpRequestConfig)
                .durationInMilliSeconds(1000)
                .repeatCount(5)
                .intervalInMilliSeconds(1000)
                .build();

        when(monitoringJobService.addJob(any(MonitoringJob.class))).thenReturn(monitoringJob);

        mockMvc.perform(post("/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Test Job\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"" + jobId + "\",\"httpRequestConfig\":{\"url\":\"https://test.com\",\"method\":\"GET\",\"headers\":{}},\"durationInMilliSeconds\":1000,\"repeatCount\":5,\"intervalInMilliSeconds\":1000}"));
    }

    @Test
    void givenValidJobIdWhenDeleteJobThenReturnSuccessful() throws Exception {
        doNothing().when(monitoringJobService).deleteJob(any(UUID.class));

        mockMvc.perform(post("/jobs/" + jobId + "/delete"))
                .andExpect(status().isOk())
                .andExpect(content().string("Job deleted"));
    }

    @Test
    void givenValidJobIdWhenTriggerJobThenReturnSuccessful() throws Exception {
        doNothing().when(monitoringJobService).triggerJob(any(UUID.class));

        mockMvc.perform(post("/jobs/" + jobId + "/trigger"))
                .andExpect(status().isOk())
                .andExpect(content().string("Job triggered"));
    }

    @Test
    void givenExceptionWhenAddJobThenReturnBadRequest() throws Exception {
        when(monitoringJobService.addJob(any(MonitoringJob.class))).thenThrow(new RuntimeException("Add job failed"));

        mockMvc.perform(post("/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Test Job\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Add job failed"));
    }

    @Test
    void givenExceptionWhenDeleteJobThenReturnBadRequest() throws Exception {
        Mockito.doThrow(new RuntimeException("Delete job failed")).when(monitoringJobService).deleteJob(any(UUID.class));

        mockMvc.perform(post("/jobs/" + jobId + "/delete"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Delete job failed"));
    }

    @Test
    void givenExceptionWhenTriggerJobThenReturnBadRequest() throws Exception {
        Mockito.doThrow(new RuntimeException("Trigger job failed")).when(monitoringJobService).triggerJob(any(UUID.class));

        mockMvc.perform(post("/jobs/" + jobId + "/trigger"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Trigger job failed"));
    }

    @Test
    void givenValidJobIdWhenGetJobThenReturnSuccessful() throws Exception {
        UUID jobId = UUID.randomUUID();
        HttpRequestConfig httpRequestConfig = new HttpRequestConfig("https://test.com", "GET", Collections.emptyMap(), null);
        MonitoringJob monitoringJob = MonitoringJob.builder()
                .id(jobId)
                .httpRequestConfig(httpRequestConfig)
                .durationInMilliSeconds(1000)
                .repeatCount(5)
                .intervalInMilliSeconds(1000)
                .build();

        when(monitoringJobService.getJob(any(UUID.class))).thenReturn(monitoringJob);

        mockMvc.perform(get("/jobs/" + jobId))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"" + jobId + "\",\"httpRequestConfig\":{\"url\":\"https://test.com\",\"method\":\"GET\",\"headers\":{}},\"durationInMilliSeconds\":1000,\"repeatCount\":5,\"intervalInMilliSeconds\":1000}"));
    }

    @Test
    void givenJobNotFoundWhenGetJobThenReturnNotFound() throws Exception {
        UUID jobId = UUID.randomUUID();
        when(monitoringJobService.getJob(any(UUID.class))).thenThrow(new MonitoringNotFoundException("Job not found"));

        mockMvc.perform(get("/jobs/" + jobId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Job not found"));
    }

    @Test
    void givenExceptionWhenGetJobThenReturnBadRequest() throws Exception {
        UUID jobId = UUID.randomUUID();
        when(monitoringJobService.getJob(any(UUID.class))).thenThrow(new RuntimeException("Get job failed"));

        mockMvc.perform(get("/jobs/" + jobId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Get job failed"));
    }
}