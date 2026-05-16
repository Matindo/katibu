package com.katibu.repository;

import com.katibu.domain.entity.PublicLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PublicLinkRepository extends JpaRepository<PublicLink, UUID> {
    Optional<PublicLink> findByTokenAndActiveTrue(String token);
    List<PublicLink> findByProjectIdOrderByCreatedAtDesc(UUID projectId);
    Optional<PublicLink> findByIdAndProjectId(UUID id, UUID projectId);
}
