package com.dc.monitoringtool.adapter.http;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HttpRequestServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private HttpRequestServiceImpl httpRequestService;

    @Test
    public void givenValidRequestWhenExecuteRequestThenReturnResponseEntity() {
        String url = "https://example.com";
        HttpMethod method = HttpMethod.GET;
        Object requestBody = null;
        Class<String> responseType = String.class;
        ResponseEntity<String> responseEntity = new ResponseEntity<>("response", HttpStatus.OK);

        when(restTemplate.exchange(eq(url), eq(method), any(HttpEntity.class), eq(responseType)))
                .thenReturn(responseEntity);

        ResponseEntity<String> result = httpRequestService.executeRequest(method, url, requestBody, responseType);

        assertEquals(responseEntity, result);
    }

}