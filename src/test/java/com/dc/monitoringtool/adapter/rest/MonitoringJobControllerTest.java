package com.dc.monitoringtool.adapter.rest;

import com.dc.monitoringtool.application.MonitoringJobOrchestrator;
import com.dc.monitoringtool.domain.model.MonitoringJob;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MonitoringJobController.class)
class MonitoringJobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MonitoringJobOrchestrator monitoringJobOrchestrator;

    private UUID jobId;

    @BeforeEach
    void setUp() {
        jobId = UUID.randomUUID();
    }

    @Test
    void addJob_successful() throws Exception {
        final MonitoringJob monitoringJob = MonitoringJob.builder()
                .id(jobId)
                .url("https://test.com")
                .durationInMilliSeconds(1000)
                .repeatCount(5)
                .intervalInMilliSeconds(1000)
                .build();

        when(monitoringJobOrchestrator.addJob(any(MonitoringJob.class))).thenReturn(monitoringJob);

        mockMvc.perform(post("/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Test Job\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"" + jobId + "\",\"url\":\"https://test.com\",\"durationInMilliSeconds\":1000,\"repeatCount\":5,\"intervalInMilliSeconds\":1000}"));
    }

    @Test
    void deleteJob_successful() throws Exception {
        doNothing().when(monitoringJobOrchestrator).deleteJob(any(UUID.class));

        mockMvc.perform(post("/jobs/" + jobId + "/delete"))
                .andExpect(status().isOk())
                .andExpect(content().string("Job deleted"));
    }

    @Test
    void triggerJob_successful() throws Exception {
        doNothing().when(monitoringJobOrchestrator).triggerJob(any(UUID.class));

        mockMvc.perform(post("/jobs/" + jobId + "/trigger"))
                .andExpect(status().isOk())
                .andExpect(content().string("Job triggered"));
    }

    @Test
    void addJob_exceptionThrown() throws Exception {
        when(monitoringJobOrchestrator.addJob(any(MonitoringJob.class))).thenThrow(new RuntimeException("Add job failed"));

        mockMvc.perform(post("/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Test Job\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Add job failed"));
    }

    @Test
    void deleteJob_exceptionThrown() throws Exception {
        Mockito.doThrow(new RuntimeException("Delete job failed")).when(monitoringJobOrchestrator).deleteJob(any(UUID.class));

        mockMvc.perform(post("/jobs/" + jobId + "/delete"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Delete job failed"));
    }

    @Test
    void triggerJob_exceptionThrown() throws Exception {
        Mockito.doThrow(new RuntimeException("Trigger job failed")).when(monitoringJobOrchestrator).triggerJob(any(UUID.class));

        mockMvc.perform(post("/jobs/" + jobId + "/trigger"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Trigger job failed"));
    }
}