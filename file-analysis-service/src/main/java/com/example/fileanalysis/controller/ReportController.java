package com.example.fileanalysis.controller;

import com.example.fileanalysis.model.Report;
import com.example.fileanalysis.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/works")
@RequiredArgsConstructor
public class ReportController {

    private final ReportRepository reportRepository;

    @GetMapping("/{workId}/reports")
    public ResponseEntity<List<Report>> getReportsByWorkId(@PathVariable Long workId) {
        List<Report> reports = reportRepository.findByWorkId(workId);
        if (reports.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reports);
    }
}