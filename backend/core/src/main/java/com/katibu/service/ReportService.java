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
import java.util.Comparator;
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

    @Transactional(readOnly = true)
    public GeneralLedgerReport generalLedger(UUID projectId, LocalDate startDate, LocalDate endDate, String userEmail) {
        Project project = projectService.requireAccessible(projectId, userEmail);
        return buildGeneralLedger(project, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public GeneralLedgerReport generalLedgerPublic(Project project, LocalDate startDate, LocalDate endDate) {
        return buildGeneralLedger(project, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public TrialBalanceReport trialBalance(UUID projectId, LocalDate asAt, String userEmail) {
        Project project = projectService.requireAccessible(projectId, userEmail);
        return buildTrialBalance(project, asAt);
    }

    @Transactional(readOnly = true)
    public TrialBalanceReport trialBalancePublic(Project project, LocalDate asAt) {
        return buildTrialBalance(project, asAt);
    }

    @Transactional(readOnly = true)
    public BalanceSheetReport balanceSheet(UUID projectId, LocalDate asAt, String userEmail) {
        Project project = projectService.requireAccessible(projectId, userEmail);
        return buildBalanceSheet(project, asAt);
    }

    @Transactional(readOnly = true)
    public BalanceSheetReport balanceSheetPublic(Project project, LocalDate asAt) {
        return buildBalanceSheet(project, asAt);
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

    private GeneralLedgerReport buildGeneralLedger(Project project, LocalDate startDate, LocalDate endDate) {
        BigDecimal opening = netPosition(entryRepository.findByProjectIdBeforeDate(project.getId(), startDate));
        List<LedgerEntry> period = entryRepository.findByProjectIdAndDateRange(project.getId(), startDate, endDate)
                .stream().sorted(Comparator.comparing(LedgerEntry::getTransactionDate)).collect(Collectors.toList());

        List<GeneralLedgerReport.Line> lines = new ArrayList<>();
        BigDecimal runningBalance = opening;
        BigDecimal totalDebits = BigDecimal.ZERO;
        BigDecimal totalCredits = BigDecimal.ZERO;

        for (LedgerEntry e : period) {
            // Cash account: inflows = DR (cash received), outflows = CR (cash paid)
            BigDecimal debit = e.getEntryType().isInflow() ? e.getAmount() : BigDecimal.ZERO;
            BigDecimal credit = e.getEntryType().isInflow() ? BigDecimal.ZERO : e.getAmount();
            runningBalance = runningBalance.add(debit).subtract(credit);
            totalDebits = totalDebits.add(debit);
            totalCredits = totalCredits.add(credit);
            lines.add(new GeneralLedgerReport.Line(
                    e.getTransactionDate(), formatLabel(e.getEntryType()),
                    e.getDescription(), e.getReference(),
                    debit, credit, runningBalance));
        }

        return new GeneralLedgerReport(project.getId(), project.getName(), startDate, endDate,
                opening, lines, totalDebits, totalCredits, runningBalance, LocalDateTime.now());
    }

    private TrialBalanceReport buildTrialBalance(Project project, LocalDate asAt) {
        List<LedgerEntry> all = entryRepository.findByProjectIdUpToDate(project.getId(), asAt);

        BigDecimal cashBalance = netPosition(all);

        Map<EntryType, BigDecimal> byType = all.stream()
                .collect(Collectors.groupingBy(LedgerEntry::getEntryType,
                        Collectors.reducing(BigDecimal.ZERO, LedgerEntry::getAmount, BigDecimal::add)));

        List<TrialBalanceReport.AccountLine> accounts = new ArrayList<>();
        // Cash/Bank is an asset — debit balance
        accounts.add(new TrialBalanceReport.AccountLine("CASH", "Cash / Bank", cashBalance, BigDecimal.ZERO));

        for (Map.Entry<EntryType, BigDecimal> e : byType.entrySet()) {
            EntryType type = e.getKey();
            BigDecimal amt = e.getValue();
            // Income types (inflows) → credit; expense types (outflows) → debit
            if (type.isInflow()) {
                accounts.add(new TrialBalanceReport.AccountLine(type.name(), formatLabel(type), BigDecimal.ZERO, amt));
            } else {
                accounts.add(new TrialBalanceReport.AccountLine(type.name(), formatLabel(type), amt, BigDecimal.ZERO));
            }
        }

        BigDecimal totalDebits = accounts.stream().map(TrialBalanceReport.AccountLine::debit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalCredits = accounts.stream().map(TrialBalanceReport.AccountLine::credit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new TrialBalanceReport(project.getId(), project.getName(), asAt,
                accounts, totalDebits, totalCredits, LocalDateTime.now());
    }

    private BalanceSheetReport buildBalanceSheet(Project project, LocalDate asAt) {
        List<LedgerEntry> all = entryRepository.findByProjectIdUpToDate(project.getId(), asAt);

        BigDecimal cash = netPosition(all);

        BigDecimal loansReceived = sumByType(all, EntryType.LOAN_RECEIVED);
        BigDecimal loansRepaid = sumByType(all, EntryType.LOAN_REPAYMENT);
        BigDecimal outstandingLoans = loansReceived.subtract(loansRepaid).max(BigDecimal.ZERO);
        BigDecimal debts = sumByType(all, EntryType.DEBT_PAYMENT);

        List<BalanceSheetReport.LiabilityLine> liabilityLines = new ArrayList<>();
        if (outstandingLoans.compareTo(BigDecimal.ZERO) > 0) {
            liabilityLines.add(new BalanceSheetReport.LiabilityLine("Outstanding Loans", outstandingLoans));
        }
        if (debts.compareTo(BigDecimal.ZERO) > 0) {
            liabilityLines.add(new BalanceSheetReport.LiabilityLine("Outstanding Debts", debts));
        }

        BigDecimal totalLiabilities = outstandingLoans.add(debts);
        BigDecimal totalEquity = cash.subtract(totalLiabilities);
        BigDecimal contributedCapital = sumByType(all, EntryType.INITIAL_CAPITAL);
        BigDecimal retainedSurplus = totalEquity.subtract(contributedCapital);

        return new BalanceSheetReport(project.getId(), project.getName(), asAt,
                cash, cash, liabilityLines, totalLiabilities,
                contributedCapital, retainedSurplus, totalEquity, LocalDateTime.now());
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
