package com.katibu.repository;

import com.katibu.domain.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {

    @Query("""
            SELECT DISTINCT p FROM Project p
            WHERE p.creator.id = :userId
               OR EXISTS (
                   SELECT 1 FROM ProjectMember m
                   WHERE m.project = p AND m.user.id = :userId
               )
            ORDER BY p.createdAt DESC
            """)
    List<Project> findAccessibleByUserId(@Param("userId") UUID userId);
}
