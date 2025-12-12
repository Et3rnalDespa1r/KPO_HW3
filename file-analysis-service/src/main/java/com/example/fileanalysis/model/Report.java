package com.example.fileanalysis.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity @Data
@Table(name = "analysis_reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long workId;
    private Double antiplagiatScore;
    private boolean isPlagiarism;
    private LocalDateTime analysisDate = LocalDateTime.now();
    private String wordCloudUrl;
}