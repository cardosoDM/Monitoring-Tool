package com.dc.monitoringtool.adapter.http;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public interface HttpRequestService {
    <T> ResponseEntity<T> executeRequest(HttpMethod method, String url, Object requestBody, Class<T> responseType);
}