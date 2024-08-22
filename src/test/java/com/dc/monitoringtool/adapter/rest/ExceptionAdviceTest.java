package com.dc.monitoringtool.adapter.rest;

import com.dc.monitoringtool.domain.exception.MonitoringException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ExceptionAdviceTest {

    @InjectMocks
    private ExceptionAdvice exceptionAdvice;

    private MonitoringException monitoringException;
    private RuntimeException runtimeException;

    @BeforeEach
    void setUp() {
        monitoringException = new MonitoringException("Monitoring error occurred");
        runtimeException = new RuntimeException("Runtime error occurred");
    }

    @Test
    void handleMonitoringException() {
        ResponseEntity<String> response = exceptionAdvice.handleMonitoringException(monitoringException);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Monitoring error occurred", response.getBody());
    }

    @Test
    void handleRuntimeException() {
        ResponseEntity<String> response = exceptionAdvice.handleMonitoringException(runtimeException);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Runtime error occurred", response.getBody());
    }
}