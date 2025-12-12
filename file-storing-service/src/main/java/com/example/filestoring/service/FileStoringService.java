package com.example.filestoring.service;
import com.example.filestoring.client.FileAnalysisClient;
import com.example.filestoring.model.WorkMetadata;
import com.example.filestoring.model.WorkStatus;
import com.example.filestoring.repository.WorkMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;

@Service
@RequiredArgsConstructor
public class FileStoringService {

    private final WorkMetadataRepository repository;
    private final FileAnalysisClient analysisClient;

    private static final String UPLOAD_DIR = "./local-storage/";

    public WorkMetadata submitWork(String submitterName, String assignmentName, MultipartFile file) throws Exception {

        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        String fileName = submitterName + "_" + file.getOriginalFilename();
        File dest = new File(uploadDir.getAbsolutePath() + "/" + fileName);
        file.transferTo(dest);

        String filePath = dest.getAbsolutePath();

        WorkMetadata metadata = new WorkMetadata();
        metadata.setSubmitterName(submitterName);
        metadata.setAssignmentName(assignmentName);
        metadata.setFilePath(filePath);
        metadata = repository.save(metadata);

        try {
            metadata.setStatus(WorkStatus.ANALYZING);
            repository.save(metadata);

            analysisClient.requestAnalysis(metadata.getId(), filePath);


        } catch (Exception e) {
            metadata.setStatus(WorkStatus.FAILED);
            repository.save(metadata);
            throw new RuntimeException("Analysis initiation failed.", e);
        }

        return metadata;
    }

    public WorkMetadata updateWorkStatus(Long workId, WorkStatus status) {
        WorkMetadata metadata = repository.findById(workId)
                .orElseThrow(() -> new RuntimeException("Work not found"));
        metadata.setStatus(status);
        return repository.save(metadata);
    }
}