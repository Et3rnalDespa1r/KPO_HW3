package com.example.fileanalysis.service;

import com.example.fileanalysis.model.Report;
import com.example.fileanalysis.model.WorkData;
import com.example.fileanalysis.repository.ReportRepository;
import com.example.fileanalysis.repository.WorkDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AnalysisService {
    private final ReportRepository reportRepository;
    private final WorkDataRepository workDataRepository;

    public AnalysisService(ReportRepository reportRepository, WorkDataRepository workDataRepository) {
        this.reportRepository = reportRepository;
        this.workDataRepository = workDataRepository;
    }

    @Transactional
    public Report analyzeWork(AnalysisRequest request) {

        boolean isPlagiarized = workDataRepository.findByFileHash(request.fileHash()).isPresent();

        double antiplagiatScore;
        if (isPlagiarized) {
            antiplagiatScore = 15.0 + new Random().nextDouble() * 10;
        } else {
            antiplagiatScore = 85.0 + new Random().nextDouble() * 10;
        }

        WorkData newWorkData = new WorkData();
        newWorkData.setId(request.workId());
        newWorkData.setFileHash(request.fileHash());
        workDataRepository.save(newWorkData);

        Report report = new Report();
        report.setWorkId(request.workId());
        report.setAntiplagiatScore(antiplagiatScore);
        report.setPlagiarism(antiplagiatScore < 85.0);
        report.setAnalysisDate(LocalDateTime.now());

        String wordCloudText = request.filePath()
                .substring(request.filePath().lastIndexOf('/') + 1)
                .replace('_', ' ')
                .replaceFirst("\\..*", "");

        report.setWordCloudUrl(createWordCloudUrl(wordCloudText));

        return reportRepository.save(report);
    }

    private String createWordCloudUrl(String text) {
        String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);
        return "https://quickchart.io/wordcloud?text=" + encodedText + "&width=500&height=300&fontFamily=sans-serif";
    }
}