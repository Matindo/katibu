package com.katibu.service;

import com.katibu.domain.entity.LedgerEntry;
import com.katibu.domain.entity.Project;
import com.katibu.domain.enums.EntryType;
import com.katibu.dto.response.report.*;
import com.katibu.repository.LedgerEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final LedgerEntryRepository entryRepository;
    private final ProjectService projectService;

    @Transactional(readOnly = true)
    public SummaryReport summary(UUID projectId, LocalDate startDate, LocalDate endDate, String userEmail) {
        Project project = projectService.requireAccessible(projectId, userEmail);
        return buildSummary(project, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public SummaryReport summaryPublic(Project project, LocalDate startDate, LocalDate endDate) {
        return buildSummary(project, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public ReceiptsPaymentsReport receiptsPayments(UUID projectId, LocalDate startDate, LocalDate endDate, String userEmail) {
        Project project = projectService.requireAccessible(projectId, userEmail);
        return buildReceiptsPayments(project, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public ReceiptsPaymentsReport receiptsPaymentsPublic(Project project, LocalDate startDate, LocalDate endDate) {
        return buildReceiptsPayments(project, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public CashFlowReport cashFlow(UUID projectId, LocalDate startDate, LocalDate endDate, String userEmail) {
        Project project = projectService.requireAccessible(projectId, userEmail);
        return buildCashFlow(project, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public CashFlowReport cashFlowPublic(Project project, LocalDate startDate, LocalDate endDate) {
        return buildCashFlow(project, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public FinancialPositionReport financialPosition(UUID projectId, LocalDate asAt, String userEmail) {
        Project project = projectService.requireAccessible(projectId, userEmail);
        return buildFinancialPosition(project, asAt);
    }

    @Transactional(readOnly = true)
    public FinancialPositionReport financialPositionPublic(Project project, LocalDate asAt) {
        return buildFinancialPosition(project, asAt);
    }

    private SummaryReport buildSummary(Project project, LocalDate startDate, LocalDate endDate) {
        BigDecimal opening = netPosition(entryRepository.findByProjectIdBeforeDate(project.getId(), startDate));
        List<LedgerEntry> period = entryRepository.findByProjectIdAndDateRange(project.getId(), startDate, endDate);
        BigDecimal receipts = sumInflows(period);
        BigDecimal payments = sumOutflows(period);
        BigDecimal net = receipts.subtract(payments);
        return new SummaryReport(project.getId(), project.getName(), startDate, endDate,
                opening, receipts, payments, net, opening.add(net), period.size(), LocalDateTime.now());
    }

    private ReceiptsPaymentsReport buildReceiptsPayments(Project project, LocalDate startDate, LocalDate endDate) {
        BigDecimal opening = netPosition(entryRepository.findByProjectIdBeforeDate(project.getId(), startDate));
        List<LedgerEntry> period = entryRepository.findByProjectIdAndDateRange(project.getId(), startDate, endDate);

        Map<EntryType, BigDecimal> byType = period.stream()
                .collect(Collectors.groupingBy(LedgerEntry::getEntryType,
                        Collectors.reducing(BigDecimal.ZERO, LedgerEntry::getAmount, BigDecimal::add)));

        List<ReceiptsPaymentsReport.LineItem> receipts = new ArrayList<>();
        List<ReceiptsPaymentsReport.LineItem> payments = new ArrayList<>();

        for (Map.Entry<EntryType, BigDecimal> e : byType.entrySet()) {
            String label = formatLabel(e.getKey());
            if (e.getKey().isInflow()) {
                receipts.add(new ReceiptsPaymentsReport.LineItem(e.getKey(), label, e.getValue()));
            } else {
                payments.add(new ReceiptsPaymentsReport.LineItem(e.getKey(), label, e.getValue()));
            }
        }

        BigDecimal totalReceipts = receipts.stream().map(ReceiptsPaymentsReport.LineItem::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalPayments = payments.stream().map(ReceiptsPaymentsReport.LineItem::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal net = totalReceipts.subtract(totalPayments);

        return new ReceiptsPaymentsReport(project.getId(), project.getName(), startDate, endDate,
                opening, receipts, totalReceipts, payments, totalPayments, net, opening.add(net), LocalDateTime.now());
    }

    private CashFlowReport buildCashFlow(Project project, LocalDate startDate, LocalDate endDate) {
        BigDecimal opening = netPosition(entryRepository.findByProjectIdBeforeDate(project.getId(), startDate));
        List<LedgerEntry> period = entryRepository.findByProjectIdAndDateRange(project.getId(), startDate, endDate);

        List<CashFlowReport.ActivityItem> operating = new ArrayList<>();
        List<CashFlowReport.ActivityItem> investing = new ArrayList<>();
        List<CashFlowReport.ActivityItem> financing = new ArrayList<>();

        Map<EntryType, BigDecimal> byType = period.stream()
                .collect(Collectors.groupingBy(LedgerEntry::getEntryType,
                        Collectors.reducing(BigDecimal.ZERO, LedgerEntry::getAmount, BigDecimal::add)));

        for (Map.Entry<EntryType, BigDecimal> e : byType.entrySet()) {
            EntryType type = e.getKey();
            BigDecimal amt = type.isInflow() ? e.getValue() : e.getValue().negate();
            CashFlowReport.ActivityItem item = new CashFlowReport.ActivityItem(formatLabel(type), amt, type.isInflow());
            switch (type.cashFlowCategory()) {
                case OPERATING -> operating.add(item);
                case INVESTING -> investing.add(item);
                case FINANCING -> financing.add(item);
            }
        }

        BigDecimal netOp = sum(operating);
        BigDecimal netInv = sum(investing);
        BigDecimal netFin = sum(financing);
        BigDecimal netTotal = netOp.add(netInv).add(netFin);

        return new CashFlowReport(project.getId(), project.getName(), startDate, endDate,
                opening, operating, netOp, investing, netInv, financing, netFin,
                netTotal, opening.add(netTotal), LocalDateTime.now());
    }

    private FinancialPositionReport buildFinancialPosition(Project project, LocalDate asAt) {
        List<LedgerEntry> all = entryRepository.findByProjectIdUpToDate(project.getId(), asAt);

        BigDecimal cash = netPosition(all);

        BigDecimal loansReceived = sumByType(all, EntryType.LOAN_RECEIVED);
        BigDecimal loansRepaid = sumByType(all, EntryType.LOAN_REPAYMENT);
        BigDecimal outstandingLoans = loansReceived.subtract(loansRepaid).max(BigDecimal.ZERO);

        BigDecimal debts = sumByType(all, EntryType.DEBT_PAYMENT);

        BigDecimal totalLiabilities = outstandingLoans.add(debts);
        BigDecimal netAssets = cash.subtract(totalLiabilities);

        BigDecimal initialCapital = sumByType(all, EntryType.INITIAL_CAPITAL);
        BigDecimal accumulated = netAssets.subtract(initialCapital);

        return new FinancialPositionReport(project.getId(), project.getName(), asAt,
                cash, cash, outstandingLoans, debts, totalLiabilities,
                netAssets, initialCapital, accumulated, LocalDateTime.now());
    }

    private BigDecimal netPosition(List<LedgerEntry> entries) {
        return sumInflows(entries).subtract(sumOutflows(entries));
    }

    private BigDecimal sumInflows(List<LedgerEntry> entries) {
        return entries.stream().filter(e -> e.getEntryType().isInflow())
                .map(LedgerEntry::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal sumOutflows(List<LedgerEntry> entries) {
        return entries.stream().filter(e -> !e.getEntryType().isInflow())
                .map(LedgerEntry::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal sumByType(List<LedgerEntry> entries, EntryType type) {
        return entries.stream().filter(e -> e.getEntryType() == type)
                .map(LedgerEntry::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal sum(List<CashFlowReport.ActivityItem> items) {
        return items.stream().map(CashFlowReport.ActivityItem::amount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String formatLabel(EntryType type) {
        return type.name().replace("_", " ");
    }
}
