package com.example.filestoring.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity @Data
@Table(name = "work_metadata")
public class WorkMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String submitterName;
    private String assignmentName;
    private LocalDateTime submissionDate = LocalDateTime.now();
    private String filePath;
    private String fileHash;
    @Enumerated(EnumType.STRING)
    private WorkStatus status = WorkStatus.PENDING;
}