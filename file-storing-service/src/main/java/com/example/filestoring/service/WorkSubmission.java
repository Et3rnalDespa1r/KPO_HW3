package com.example.filestoring.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.Serializable;

public record WorkSubmission(
        String submitterName,
        String assignmentName,
        MultipartFile file
) implements Serializable {}