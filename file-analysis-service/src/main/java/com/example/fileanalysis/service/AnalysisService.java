package com.example.fileanalysis.service;// HW3/file-analysis-service/src/main/java/com.example.filestoring.service.AnalysisService.java
import com.example.fileanalysis.model.Report;
import com.example.fileanalysis.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AnalysisService {

    private final ReportRepository reportRepository;

    @Transactional
    public void analyze(AnalysisRequest request) throws IOException {
        String fileContent = "Sample content for assignment " + request.workId();
        Random rand = new Random();
        double score = 65 + rand.nextDouble() * 35;
        boolean isPlagiarism = score < 85.0;
        String wordCloudUrl = createWordCloudUrl(fileContent);
        Report report = new Report();
        report.setWorkId(request.workId());
        report.setAntiplagiatScore(score);
        report.setPlagiarism(isPlagiarism);
        report.setWordCloudUrl(wordCloudUrl);
        reportRepository.save(report);
    }

    private String createWordCloudUrl(String text) {
        String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);
        return "https://quickchart.io/wordcloud?text=" + encodedText + "&width=500&height=300&fontFamily=sans-serif";
    }
}