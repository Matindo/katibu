package com.katibu.dto.response.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CashFlowReport(
        UUID projectId,
        String projectName,
        LocalDate periodStart,
        LocalDate periodEnd,
        BigDecimal openingBalance,
        List<ActivityItem> operatingActivities,
        BigDecimal netOperating,
        List<ActivityItem> investingActivities,
        BigDecimal netInvesting,
        List<ActivityItem> financingActivities,
        BigDecimal netFinancing,
        BigDecimal netCashFlow,
        BigDecimal closingBalance,
        LocalDateTime generatedAt
) {
    public record ActivityItem(String label, BigDecimal amount, boolean inflow) {}
}
