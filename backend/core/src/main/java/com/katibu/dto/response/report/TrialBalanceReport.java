package com.katibu.dto.response.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record TrialBalanceReport(
        UUID projectId,
        String projectName,
        LocalDate asAt,
        List<AccountLine> accounts,
        BigDecimal totalDebits,
        BigDecimal totalCredits,
        LocalDateTime generatedAt
) {
    public record AccountLine(
            String account,
            String label,
            BigDecimal debit,
            BigDecimal credit
    ) {}
}
