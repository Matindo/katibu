package com.katibu.controller;

import com.katibu.dto.response.ApiResponse;
import com.katibu.dto.response.report.*;
import com.katibu.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects/{projectId}/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/summary")
    public ApiResponse<SummaryReport> summary(
            @PathVariable UUID projectId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Authentication auth) {
        return ApiResponse.ok(reportService.summary(projectId, startDate, endDate, auth.getName()));
    }

    @GetMapping("/receipts-payments")
    public ApiResponse<ReceiptsPaymentsReport> receiptsPayments(
            @PathVariable UUID projectId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Authentication auth) {
        return ApiResponse.ok(reportService.receiptsPayments(projectId, startDate, endDate, auth.getName()));
    }

    @GetMapping("/cash-flow")
    public ApiResponse<CashFlowReport> cashFlow(
            @PathVariable UUID projectId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Authentication auth) {
        return ApiResponse.ok(reportService.cashFlow(projectId, startDate, endDate, auth.getName()));
    }

    @GetMapping("/financial-position")
    public ApiResponse<FinancialPositionReport> financialPosition(
            @PathVariable UUID projectId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate asAt,
            Authentication auth) {
        return ApiResponse.ok(reportService.financialPosition(projectId, asAt, auth.getName()));
    }
}
