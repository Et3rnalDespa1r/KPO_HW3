package com.example.filestoring.controller;
import com.example.filestoring.model.WorkMetadata;
import com.example.filestoring.model.WorkStatus;
import com.example.filestoring.service.FileStoringService;
import com.example.filestoring.service.WorkSubmission; // НОВЫЙ ИМПОРТ
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/internal/store")
@RequiredArgsConstructor
public class FileStoringController {

    private final FileStoringService fileStoringService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> uploadWork(
            @RequestParam("submitterName") String submitterName,
            @RequestParam("assignmentName") String assignmentName,
            @RequestParam("file") MultipartFile file) {
        try {
            // Создаем объект WorkSubmission
            WorkSubmission submission = new WorkSubmission(submitterName, assignmentName, file);

            WorkMetadata metadata = fileStoringService.submitWork(submission, file);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(metadata.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }
    @PutMapping("/{workId}/status")
    public ResponseEntity<WorkMetadata> updateStatus(@PathVariable Long workId, @RequestParam WorkStatus status) {
        try {
            WorkMetadata updated = fileStoringService.updateWorkStatus(workId, status);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}