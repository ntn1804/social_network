package com.example.socialnetwork.repository;

import com.example.socialnetwork.entity.FileData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileDataRepo extends JpaRepository<FileData, Long> {
    FileData findByName(String fileName);

    FileData findByFilePath(String filePath);
}
