package com.katibu.dto.response.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record GeneralLedgerReport(
        UUID projectId,
        String projectName,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal openingBalance,
        List<Line> lines,
        BigDecimal totalDebits,
        BigDecimal totalCredits,
        BigDecimal closingBalance,
        LocalDateTime generatedAt
) {
    public record Line(
            LocalDate date,
            String entryType,
            String description,
            String reference,
            BigDecimal debit,
            BigDecimal credit,
            BigDecimal balance
    ) {}
}
