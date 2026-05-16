package com.katibu.controller;

import com.katibu.domain.entity.Project;
import com.katibu.dto.response.ApiResponse;
import com.katibu.dto.response.report.*;
import com.katibu.service.PublicLinkService;
import com.katibu.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicController {

    private final PublicLinkService publicLinkService;
    private final ReportService reportService;

    @GetMapping("/{token}/summary")
    public ApiResponse<SummaryReport> summary(
            @PathVariable String token,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Project project = publicLinkService.resolveToken(token);
        return ApiResponse.ok(reportService.summaryPublic(project, startDate, endDate));
    }

    @GetMapping("/{token}/receipts-payments")
    public ApiResponse<ReceiptsPaymentsReport> receiptsPayments(
            @PathVariable String token,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Project project = publicLinkService.resolveToken(token);
        return ApiResponse.ok(reportService.receiptsPaymentsPublic(project, startDate, endDate));
    }

    @GetMapping("/{token}/cash-flow")
    public ApiResponse<CashFlowReport> cashFlow(
            @PathVariable String token,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Project project = publicLinkService.resolveToken(token);
        return ApiResponse.ok(reportService.cashFlowPublic(project, startDate, endDate));
    }

    @GetMapping("/{token}/financial-position")
    public ApiResponse<FinancialPositionReport> financialPosition(
            @PathVariable String token,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate asAt) {
        Project project = publicLinkService.resolveToken(token);
        return ApiResponse.ok(reportService.financialPositionPublic(project, asAt));
    }

    @GetMapping("/{token}/general-ledger")
    public ApiResponse<GeneralLedgerReport> generalLedger(
            @PathVariable String token,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Project project = publicLinkService.resolveToken(token);
        return ApiResponse.ok(reportService.generalLedgerPublic(project, startDate, endDate));
    }

    @GetMapping("/{token}/trial-balance")
    public ApiResponse<TrialBalanceReport> trialBalance(
            @PathVariable String token,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate asAt) {
        Project project = publicLinkService.resolveToken(token);
        return ApiResponse.ok(reportService.trialBalancePublic(project, asAt));
    }

    @GetMapping("/{token}/balance-sheet")
    public ApiResponse<BalanceSheetReport> balanceSheet(
            @PathVariable String token,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate asAt) {
        Project project = publicLinkService.resolveToken(token);
        return ApiResponse.ok(reportService.balanceSheetPublic(project, asAt));
    }
}
