package com.katibu.dto.response.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record SummaryReport(
        UUID projectId,
        String projectName,
        LocalDate periodStart,
        LocalDate periodEnd,
        BigDecimal openingBalance,
        BigDecimal totalReceipts,
        BigDecimal totalPayments,
        BigDecimal netCashFlow,
        BigDecimal closingBalance,
        long totalEntries,
        LocalDateTime generatedAt
) {}
