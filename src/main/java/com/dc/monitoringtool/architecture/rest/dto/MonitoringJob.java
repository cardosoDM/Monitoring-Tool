package com.dc.monitoringtool.architecture.rest.dto;

import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class MonitoringJob {

    private final UUID id;

    private final  String url;

    private final String cronExpression;
}
