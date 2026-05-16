package com.katibu.dto.response.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record BalanceSheetReport(
        UUID projectId,
        String projectName,
        LocalDate asAt,
        BigDecimal cashAndEquivalents,
        BigDecimal totalAssets,
        List<LiabilityLine> liabilityLines,
        BigDecimal totalLiabilities,
        BigDecimal contributedCapital,
        BigDecimal retainedSurplus,
        BigDecimal totalEquity,
        LocalDateTime generatedAt
) {
    public record LiabilityLine(String description, BigDecimal amount) {}
}
