package com.example.fileanalysis.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class WorkData {
    @Id
    private Long id;

    private String fileHash;
}