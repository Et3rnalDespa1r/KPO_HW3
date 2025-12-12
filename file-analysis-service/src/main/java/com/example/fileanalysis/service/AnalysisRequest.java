package com.example.fileanalysis.service;

import java.io.Serializable;

public record AnalysisRequest(
        Long workId,
        String filePath,
        String fileHash
) implements Serializable {}