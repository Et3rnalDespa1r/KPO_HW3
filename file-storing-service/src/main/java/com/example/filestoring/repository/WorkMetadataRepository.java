package com.example.filestoring.repository;
import com.example.filestoring.model.WorkMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkMetadataRepository extends JpaRepository<WorkMetadata, Long> {}