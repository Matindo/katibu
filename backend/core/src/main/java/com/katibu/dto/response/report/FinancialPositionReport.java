package com.katibu.dto.response.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record FinancialPositionReport(
        UUID projectId,
        String projectName,
        LocalDate asAtDate,
        // Assets
        BigDecimal cashAndEquivalents,
        BigDecimal totalAssets,
        // Liabilities
        BigDecimal outstandingLoans,
        BigDecimal outstandingDebts,
        BigDecimal totalLiabilities,
        // Net assets
        BigDecimal netAssets,
        // Represented by
        BigDecimal initialCapital,
        BigDecimal accumulatedSurplusDeficit,
        LocalDateTime generatedAt
) {}
