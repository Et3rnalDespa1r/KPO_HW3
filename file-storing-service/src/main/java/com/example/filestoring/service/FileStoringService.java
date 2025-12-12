package com.example.filestoring.service;

import com.example.filestoring.client.FileAnalysisClient;
import com.example.filestoring.model.WorkMetadata;
import com.example.filestoring.model.WorkStatus;
import com.example.filestoring.repository.WorkMetadataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.client.ResourceAccessException;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Slf4j
@Service
public class FileStoringService {

    private final WorkMetadataRepository workMetadataRepository;
    private final FileAnalysisClient fileAnalysisClient;

    private static final String STORAGE_ROOT = "./local-storage/";

    public FileStoringService(WorkMetadataRepository workMetadataRepository, FileAnalysisClient fileAnalysisClient) {
        this.workMetadataRepository = workMetadataRepository;
        this.fileAnalysisClient = fileAnalysisClient;
    }

    public WorkMetadata submitWork(WorkSubmission submission, MultipartFile file) throws Exception {
        WorkMetadata metadata = new WorkMetadata();
        metadata.setSubmitterName(submission.submitterName());
        metadata.setAssignmentName(submission.assignmentName());
        metadata.setSubmissionDate(LocalDateTime.now());
        metadata.setStatus(WorkStatus.PENDING);

        metadata = workMetadataRepository.save(metadata);

        String uniqueFileName = metadata.getId() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(STORAGE_ROOT + uniqueFileName);

        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());

        metadata.setFilePath(filePath.toString());

        String hash = generateHash(file);
        metadata.setFileHash(hash);

        metadata = workMetadataRepository.save(metadata);

        try {
            metadata.setStatus(WorkStatus.ANALYZING);
            workMetadataRepository.save(metadata);

            fileAnalysisClient.analyzeWork(metadata.getId(), metadata.getFilePath(), metadata.getFileHash());

            metadata.setStatus(WorkStatus.COMPLETED);
        } catch (ResourceAccessException e) {
            log.error("Analysis initiation failed. File Analysis Service is unavailable or failed: {}", e.getMessage());
            metadata.setStatus(WorkStatus.FAILED);
        } catch (Exception e) {
            log.error("Analysis initiation failed with unexpected error: {}", e.getMessage(), e);
            metadata.setStatus(WorkStatus.FAILED);
        }

        return workMetadataRepository.save(metadata);
    }

    public WorkMetadata updateWorkStatus(Long workId, WorkStatus status) {
        return workMetadataRepository.findById(workId)
                .map(metadata -> {
                    metadata.setStatus(status);
                    return workMetadataRepository.save(metadata);
                })
                .orElseThrow(() -> new RuntimeException("Work not found with ID: " + workId));
    }

    private String generateHash(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            return DigestUtils.sha256Hex(inputStream);
        }
    }
}