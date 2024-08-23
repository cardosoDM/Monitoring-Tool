package com.dc.monitoringtool.adapter.http;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HttpRequestServiceImpl implements HttpRequestService {

    private final RestTemplate restTemplate;

    public HttpRequestServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public <T> ResponseEntity<T> executeRequest(HttpMethod method, String url, Object requestBody, Class<T> responseType) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody);
        return restTemplate.exchange(url, method, requestEntity, responseType);
    }
}