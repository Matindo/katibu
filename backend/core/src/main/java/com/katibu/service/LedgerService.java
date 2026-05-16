package com.katibu.service;

import com.katibu.domain.entity.LedgerEntry;
import com.katibu.domain.entity.Project;
import com.katibu.domain.entity.User;
import com.katibu.domain.enums.ProjectStatus;
import com.katibu.dto.request.CreateEntryRequest;
import com.katibu.dto.request.UpdateEntryRequest;
import com.katibu.dto.response.LedgerEntryResponse;
import com.katibu.exception.BusinessException;
import com.katibu.exception.ResourceNotFoundException;
import com.katibu.repository.LedgerEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LedgerService {

    private final LedgerEntryRepository entryRepository;
    private final ProjectService projectService;
    private final AuthService authService;

    @Transactional
    public LedgerEntryResponse create(UUID projectId, CreateEntryRequest req, String userEmail) {
        Project project = projectService.requireAccessible(projectId, userEmail);
        if (project.getStatus() == ProjectStatus.ARCHIVED) {
            throw new BusinessException("Cannot add entries to an archived project");
        }
        User actor = authService.requireByEmail(userEmail);
        LedgerEntry entry = LedgerEntry.builder()
                .project(project)
                .entryType(req.entryType())
                .amount(req.amount())
                .description(req.description())
                .reference(req.reference())
                .transactionDate(req.transactionDate())
                .recordedBy(actor)
                .build();
        return toResponse(entryRepository.save(entry));
    }

    @Transactional(readOnly = true)
    public List<LedgerEntryResponse> list(UUID projectId, String userEmail) {
        projectService.requireAccessible(projectId, userEmail);
        return entryRepository
                .findByProjectIdAndDeletedAtIsNullOrderByTransactionDateDesc(projectId)
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public LedgerEntryResponse get(UUID projectId, UUID entryId, String userEmail) {
        projectService.requireAccessible(projectId, userEmail);
        LedgerEntry entry = entryRepository
                .findByIdAndProjectIdAndDeletedAtIsNull(entryId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Ledger entry not found"));
        return toResponse(entry);
    }

    @Transactional
    public LedgerEntryResponse update(UUID projectId, UUID entryId, UpdateEntryRequest req, String userEmail) {
        Project project = projectService.requireAccessible(projectId, userEmail);
        if (project.getStatus() == ProjectStatus.ARCHIVED) {
            throw new BusinessException("Cannot modify entries in an archived project");
        }
        LedgerEntry entry = entryRepository
                .findByIdAndProjectIdAndDeletedAtIsNull(entryId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Ledger entry not found"));
        entry.setEntryType(req.entryType());
        entry.setAmount(req.amount());
        entry.setDescription(req.description());
        entry.setReference(req.reference());
        entry.setTransactionDate(req.transactionDate());
        return toResponse(entryRepository.save(entry));
    }

    @Transactional
    public void delete(UUID projectId, UUID entryId, String userEmail) {
        Project project = projectService.requireAccessible(projectId, userEmail);
        if (project.getStatus() == ProjectStatus.ARCHIVED) {
            throw new BusinessException("Cannot delete entries from an archived project");
        }
        LedgerEntry entry = entryRepository
                .findByIdAndProjectIdAndDeletedAtIsNull(entryId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Ledger entry not found"));
        entry.setDeletedAt(LocalDateTime.now());
        entryRepository.save(entry);
    }

    private LedgerEntryResponse toResponse(LedgerEntry e) {
        return new LedgerEntryResponse(
                e.getId(), e.getProject().getId(),
                e.getEntryType().name(), e.getEntryType().isInflow(),
                e.getAmount(), e.getDescription(), e.getReference(),
                e.getTransactionDate(),
                e.getRecordedBy().getId(), e.getRecordedBy().getFullName(),
                e.getRecordedAt());
    }
}
