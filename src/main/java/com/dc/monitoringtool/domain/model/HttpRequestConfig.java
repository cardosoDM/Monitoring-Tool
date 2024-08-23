package com.dc.monitoringtool.domain.model;

import java.io.Serializable;
import java.util.Map;

/**
 * Represents the configuration of an HTTP request.
 * <p>
 * The configuration includes the URL to send the request to,
 * the HTTP method to use,
 * the headers to include in the request,
 * and the body of the request.
 * The HttpRequestConfig is immutable.
 */
public record HttpRequestConfig(String url,
                                String method,
                                Map<String, String> headers,
                                Object body) implements Serializable {
}
