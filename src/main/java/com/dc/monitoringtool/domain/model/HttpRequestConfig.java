package com.dc.monitoringtool.domain.model;

import java.io.Serializable;
import java.util.Map;

public record HttpRequestConfig(String url,
                                String method,
                                Map<String, String> headers,
                                Object body) implements Serializable {
}
