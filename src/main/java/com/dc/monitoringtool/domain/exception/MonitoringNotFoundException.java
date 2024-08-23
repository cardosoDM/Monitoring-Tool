package com.dc.monitoringtool.domain.exception;

public class MonitoringNotFoundException extends RuntimeException {
    public MonitoringNotFoundException(String message) {
        super(message);
    }

}