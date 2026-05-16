package com.katibu.dto.response.report;

import com.katibu.domain.enums.EntryType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ReceiptsPaymentsReport(
        UUID projectId,
        String projectName,
        LocalDate periodStart,
        LocalDate periodEnd,
        BigDecimal openingBalance,
        List<LineItem> receipts,
        BigDecimal totalReceipts,
        List<LineItem> payments,
        BigDecimal totalPayments,
        BigDecimal netPosition,
        BigDecimal closingBalance,
        LocalDateTime generatedAt
) {
    public record LineItem(EntryType entryType, String label, BigDecimal amount) {}
}
