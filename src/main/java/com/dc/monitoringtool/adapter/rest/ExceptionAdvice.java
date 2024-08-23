package com.dc.monitoringtool.adapter.rest;

import com.dc.monitoringtool.domain.exception.MonitoringException;
import com.dc.monitoringtool.domain.exception.MonitoringNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler(MonitoringException.class)
    public ResponseEntity<String> handleMonitoringException(MonitoringException ex) {
        log.error("MonitoringException: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MonitoringNotFoundException.class)
    public ResponseEntity<String> handleMonitoringNotFoundException(MonitoringNotFoundException ex) {
        log.error("MonitoringNotFoundException: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleMonitoringException(RuntimeException ex) {
        log.error("RuntimeException: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}