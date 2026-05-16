package com.katibu.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record LedgerEntryResponse(
        UUID id,
        UUID projectId,
        String entryType,
        boolean inflow,
        BigDecimal amount,
        String description,
        String reference,
        LocalDate transactionDate,
        UUID recordedById,
        String recordedByName,
        LocalDateTime recordedAt
) {}
