package com.example.filestoring.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class FileAnalysisClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String analysisServiceUrl;

    public FileAnalysisClient(@Value("${analysis-service.url}") String analysisServiceUrl) {
        this.analysisServiceUrl = analysisServiceUrl;
    }

    public void requestAnalysis(Long workId, String filePath) {
        String url = analysisServiceUrl + "/internal/analyze";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestBody = String.format("{\"workId\": %d, \"filePath\": \"%s\"}", workId, filePath);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Void> response = restTemplate.postForEntity(url, entity, Void.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new RuntimeException("Analysis Service returned non-OK status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("File Analysis Service is unavailable or failed.", e);
        }
    }
}