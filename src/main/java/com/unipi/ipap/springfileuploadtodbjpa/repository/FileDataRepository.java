package com.unipi.ipap.springfileuploadtodbjpa.repository;

import com.unipi.ipap.springfileuploadtodbjpa.entity.FileData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileDataRepository extends JpaRepository<FileData, Long> {
    Optional<FileData> findByName(String fileName);
}
