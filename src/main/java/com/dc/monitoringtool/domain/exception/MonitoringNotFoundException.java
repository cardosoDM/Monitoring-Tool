package com.dc.monitoringtool.domain.exception;

public class MonitoringNotFoundException extends RuntimeException {
    public MonitoringNotFoundException(String message) {
        super(message);
    }

    public MonitoringNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}