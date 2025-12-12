package com.example.fileanalysis.controller;// HW3/file-analysis-service/src/main/java/com.example.filestoring.controller.InternalAnalysisController.java
import com.example.fileanalysis.service.AnalysisRequest;
import com.example.fileanalysis.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@RestController
@RequestMapping("/internal/analyze")
@RequiredArgsConstructor
public class InternalAnalysisController {

    private final AnalysisService analysisService;

    @PostMapping
    public ResponseEntity<Void> runAnalysis(@RequestBody AnalysisRequest request) throws IOException {
        analysisService.analyze(request);
        return ResponseEntity.ok().build();
    }
}