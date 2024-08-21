package com.dc.monitoringtool.adapter.http;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
@Slf4j
public class HttpGenericService {
    private final RestTemplate restTemplate;

    public void someRestCall(String url) {
        //TODO: Implement a rest call to the given url
        Object execute = restTemplate.execute(url, HttpMethod.GET, null, null);

        String forObject = restTemplate.getForObject(url, String.class);
        log.info("Response: {}", forObject);
    }


}
