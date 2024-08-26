package com.dc.monitoringtool.adapter.rest;

import com.dc.monitoringtool.domain.exception.MonitoringException;
import com.dc.monitoringtool.domain.exception.MonitoringNotFoundException;
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
    private MonitoringNotFoundException monitoringNotFoundException;

    @BeforeEach
    void setUp() {
        monitoringException = new MonitoringException("Monitoring error occurred");
        runtimeException = new RuntimeException("Runtime error occurred");
        monitoringNotFoundException = new MonitoringNotFoundException("Not found");
    }

    @Test
    void givenMonitoringExceptionWhenHandleExceptionThenReturnBadRequest() {
        ResponseEntity<String> response = exceptionAdvice.handleMonitoringException(monitoringException);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Monitoring error occurred", response.getBody());
    }

    @Test
    void givenRuntimeExceptionWhenHandleExceptionThenReturnBadRequest() {
        ResponseEntity<String> response = exceptionAdvice.handleMonitoringException(runtimeException);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Runtime error occurred", response.getBody());
    }

    @Test
    void givenMonitoringNotFoundExceptionWhenHandleExceptionThenReturnNotFound() {
        ResponseEntity<String> response = exceptionAdvice.handleMonitoringNotFoundException(monitoringNotFoundException);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not found", response.getBody());
    }
}