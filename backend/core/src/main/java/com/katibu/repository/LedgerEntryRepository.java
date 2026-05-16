package com.katibu.repository;

import com.katibu.domain.entity.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, UUID> {

    List<LedgerEntry> findByProjectIdAndDeletedAtIsNullOrderByTransactionDateDesc(UUID projectId);

    @Query("""
            SELECT e FROM LedgerEntry e
            WHERE e.project.id = :projectId
              AND e.deletedAt IS NULL
              AND e.transactionDate BETWEEN :startDate AND :endDate
            ORDER BY e.transactionDate ASC, e.recordedAt ASC
            """)
    List<LedgerEntry> findByProjectIdAndDateRange(
            @Param("projectId") UUID projectId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("""
            SELECT e FROM LedgerEntry e
            WHERE e.project.id = :projectId
              AND e.deletedAt IS NULL
              AND e.transactionDate < :date
            ORDER BY e.transactionDate ASC
            """)
    List<LedgerEntry> findByProjectIdBeforeDate(
            @Param("projectId") UUID projectId,
            @Param("date") LocalDate date);

    @Query("""
            SELECT e FROM LedgerEntry e
            WHERE e.project.id = :projectId
              AND e.deletedAt IS NULL
              AND e.transactionDate <= :date
            ORDER BY e.transactionDate ASC
            """)
    List<LedgerEntry> findByProjectIdUpToDate(
            @Param("projectId") UUID projectId,
            @Param("date") LocalDate date);

    Optional<LedgerEntry> findByIdAndProjectIdAndDeletedAtIsNull(UUID id, UUID projectId);
}
