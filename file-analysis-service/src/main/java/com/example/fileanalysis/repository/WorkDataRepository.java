package com.example.fileanalysis.repository;

import com.example.fileanalysis.model.WorkData;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WorkDataRepository extends JpaRepository<WorkData, Long> {
    Optional<WorkData> findByFileHash(String fileHash);
}