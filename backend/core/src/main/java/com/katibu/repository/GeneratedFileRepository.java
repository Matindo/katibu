package com.katibu.repository;

import com.katibu.domain.entity.GeneratedFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GeneratedFileRepository extends JpaRepository<GeneratedFile, UUID> {
    List<GeneratedFile> findByProjectIdOrderByCreatedAtDesc(UUID projectId);
    Optional<GeneratedFile> findByIdAndProjectId(UUID id, UUID projectId);
}
