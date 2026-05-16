package com.katibu.service;

import com.katibu.domain.entity.GeneratedFile;
import com.katibu.domain.entity.LedgerEntry;
import com.katibu.domain.entity.Project;
import com.katibu.domain.entity.User;
import com.katibu.domain.enums.EntryType;
import com.katibu.domain.enums.FileType;
import com.katibu.domain.enums.ReportType;
import com.katibu.dto.request.ExportRequest;
import com.katibu.dto.response.GeneratedFileResponse;
import com.katibu.dto.response.report.*;
import com.katibu.exception.ResourceNotFoundException;
import com.katibu.repository.GeneratedFileRepository;
import com.katibu.repository.LedgerEntryRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileExportService {

    private final GeneratedFileRepository fileRepository;
    private final LedgerEntryRepository entryRepository;
    private final ProjectService projectService;
    private final AuthService authService;
    private final ReportService reportService;
    private final MinioService minioService;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Transactional
    public GeneratedFileResponse export(UUID projectId, ExportRequest req, String userEmail) {
        Project project = projectService.requireAccessible(projectId, userEmail);
        User actor = authService.requireByEmail(userEmail);

        LocalDate start = req.startDate() != null ? req.startDate() : project.getStartDate();
        LocalDate end = req.endDate() != null ? req.endDate() : LocalDate.now();

        byte[] content;
        String contentType;
        String extension;

        try {
            if (req.fileType() == FileType.PDF) {
                content = generatePdf(project, req.reportType(), start, end);
                contentType = "application/pdf";
                extension = "pdf";
            } else {
                content = generateCsv(project, req.reportType(), start, end);
                contentType = "text/csv";
                extension = "csv";
            }
        } catch (Exception e) {
            throw new RuntimeException("File generation failed: " + e.getMessage(), e);
        }

        String fileName = buildFileName(project.getName(), req.reportType(), start, end, extension);
        String objectKey = projectId + "/" + req.reportType().name().toLowerCase() + "/"
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "_" + fileName;

        try {
            minioService.upload(objectKey, content, contentType);
        } catch (Exception e) {
            throw new RuntimeException("File upload failed: " + e.getMessage(), e);
        }

        GeneratedFile file = GeneratedFile.builder()
                .project(project)
                .fileName(fileName)
                .objectKey(objectKey)
                .fileType(req.fileType())
                .reportType(req.reportType())
                .sizeBytes((long) content.length)
                .createdBy(actor)
                .build();
        file = fileRepository.save(file);
        return toResponse(file);
    }

    @Transactional(readOnly = true)
    public List<GeneratedFileResponse> list(UUID projectId, String userEmail) {
        projectService.requireAccessible(projectId, userEmail);
        return fileRepository.findByProjectIdOrderByCreatedAtDesc(projectId).stream()
                .map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public byte[] download(UUID projectId, UUID fileId, String userEmail) {
        projectService.requireAccessible(projectId, userEmail);
        GeneratedFile file = fileRepository.findByIdAndProjectId(fileId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException("File not found"));
        try {
            return minioService.download(file.getObjectKey());
        } catch (Exception e) {
            throw new RuntimeException("File download failed: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public GeneratedFile getFile(UUID projectId, UUID fileId, String userEmail) {
        projectService.requireAccessible(projectId, userEmail);
        return fileRepository.findByIdAndProjectId(fileId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException("File not found"));
    }

    private byte[] generatePdf(Project project, ReportType reportType, LocalDate start, LocalDate end)
            throws Exception {
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);
            PDType1Font bold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            PDType1Font regular = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                float y = 780;
                float margin = 50;
                float lineHeight = 16;

                // Header
                cs.beginText();
                cs.setFont(bold, 16);
                cs.newLineAtOffset(margin, y);
                cs.showText(project.getName());
                cs.endText();
                y -= lineHeight * 1.5f;

                cs.beginText();
                cs.setFont(regular, 10);
                cs.newLineAtOffset(margin, y);
                cs.showText(formatReportTitle(reportType) + " | Period: " + start + " to " + end);
                cs.endText();
                y -= lineHeight * 2;

                // Divider line
                cs.moveTo(margin, y);
                cs.lineTo(545, y);
                cs.stroke();
                y -= lineHeight;

                // Report content
                List<String[]> rows = buildReportRows(project, reportType, start, end);
                for (String[] row : rows) {
                    if (row.length == 0) {
                        y -= lineHeight * 0.5f;
                        continue;
                    }
                    boolean isHeader = row.length > 2 && "H".equals(row[2]);
                    cs.beginText();
                    cs.setFont(isHeader ? bold : regular, isHeader ? 10 : 9);
                    cs.newLineAtOffset(margin, y);
                    cs.showText(row[0]);
                    if (row.length > 1 && !row[1].isEmpty()) {
                        cs.endText();
                        cs.beginText();
                        cs.setFont(isHeader ? bold : regular, isHeader ? 10 : 9);
                        cs.newLineAtOffset(420, y);
                        cs.showText(row[1]);
                    }
                    cs.endText();
                    y -= lineHeight;

                    if (y < 60) {
                        cs.close();
                        page = new PDPage(PDRectangle.A4);
                        doc.addPage(page);
                        y = 780;
                    }
                }

                // Footer
                cs.beginText();
                cs.setFont(regular, 8);
                cs.newLineAtOffset(margin, 30);
                cs.showText("Generated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        + " | Katibu Financial Management");
                cs.endText();
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            doc.save(out);
            return out.toByteArray();
        }
    }

    private byte[] generateCsv(Project project, ReportType reportType, LocalDate start, LocalDate end)
            throws Exception {
        if (reportType == ReportType.LEDGER) {
            return buildLedgerCsv(project, start, end);
        }
        List<String[]> rows = buildReportRows(project, reportType, start, end);
        StringWriter writer = new StringWriter();
        try (CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            printer.printRecord((Object[]) new String[]{project.getName(), reportType.name(), start.toString(), end.toString()});
            for (String[] row : rows) {
                if (row.length == 0) continue;
                printer.printRecord((Object[]) row);
            }
        }
        return writer.toString().getBytes(StandardCharsets.UTF_8);
    }

    private byte[] buildLedgerCsv(Project project, LocalDate start, LocalDate end) throws Exception {
        List<LedgerEntry> entries = entryRepository.findByProjectIdAndDateRange(project.getId(), start, end);
        StringWriter writer = new StringWriter();
        try (CSVPrinter printer = new CSVPrinter(writer,
                CSVFormat.DEFAULT.builder()
                        .setHeader("Date", "Type", "Direction", "Amount", "Description", "Reference", "Recorded By", "Recorded At")
                        .build())) {
            for (LedgerEntry e : entries) {
                printer.printRecord(
                        e.getTransactionDate(),
                        e.getEntryType().name(),
                        e.getEntryType().isInflow() ? "INFLOW" : "OUTFLOW",
                        e.getAmount(),
                        e.getDescription(),
                        e.getReference() != null ? e.getReference() : "",
                        e.getRecordedBy().getFullName(),
                        e.getRecordedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            }
        }
        return writer.toString().getBytes(StandardCharsets.UTF_8);
    }

    private List<String[]> buildReportRows(Project project, ReportType reportType, LocalDate start, LocalDate end) {
        return switch (reportType) {
            case SUMMARY -> summaryRows(reportService.summaryPublic(project, start, end));
            case RECEIPTS_PAYMENTS -> rpRows(reportService.receiptsPaymentsPublic(project, start, end));
            case CASH_FLOW -> cashFlowRows(reportService.cashFlowPublic(project, start, end));
            case FINANCIAL_POSITION -> fpRows(reportService.financialPositionPublic(project, end));
            case LEDGER -> List.of();
        };
    }

    private List<String[]> summaryRows(SummaryReport r) {
        return List.of(
                new String[]{"SUMMARY REPORT", "", "H"},
                new String[]{},
                new String[]{"Opening Balance", fmt(r.openingBalance())},
                new String[]{"Total Receipts", fmt(r.totalReceipts())},
                new String[]{"Total Payments", fmt(r.totalPayments())},
                new String[]{"Net Cash Flow", fmt(r.netCashFlow())},
                new String[]{"Closing Balance", fmt(r.closingBalance()), "H"},
                new String[]{"Total Entries", String.valueOf(r.totalEntries())}
        );
    }

    private List<String[]> rpRows(ReceiptsPaymentsReport r) {
        List<String[]> rows = new java.util.ArrayList<>();
        rows.add(new String[]{"STATEMENT OF RECEIPTS AND PAYMENTS", "", "H"});
        rows.add(new String[]{});
        rows.add(new String[]{"Opening Balance", fmt(r.openingBalance())});
        rows.add(new String[]{});
        rows.add(new String[]{"RECEIPTS", "", "H"});
        for (ReceiptsPaymentsReport.LineItem item : r.receipts()) {
            rows.add(new String[]{"  " + item.label(), fmt(item.amount())});
        }
        rows.add(new String[]{"Total Receipts", fmt(r.totalReceipts()), "H"});
        rows.add(new String[]{});
        rows.add(new String[]{"PAYMENTS", "", "H"});
        for (ReceiptsPaymentsReport.LineItem item : r.payments()) {
            rows.add(new String[]{"  " + item.label(), fmt(item.amount())});
        }
        rows.add(new String[]{"Total Payments", fmt(r.totalPayments()), "H"});
        rows.add(new String[]{});
        rows.add(new String[]{"Net Position", fmt(r.netPosition()), "H"});
        rows.add(new String[]{"Closing Balance", fmt(r.closingBalance()), "H"});
        return rows;
    }

    private List<String[]> cashFlowRows(CashFlowReport r) {
        List<String[]> rows = new java.util.ArrayList<>();
        rows.add(new String[]{"STATEMENT OF CASH FLOWS", "", "H"});
        rows.add(new String[]{});
        rows.add(new String[]{"Opening Balance", fmt(r.openingBalance())});
        rows.add(new String[]{});
        rows.add(new String[]{"OPERATING ACTIVITIES", "", "H"});
        for (CashFlowReport.ActivityItem item : r.operatingActivities()) {
            rows.add(new String[]{"  " + item.label(), fmt(item.amount())});
        }
        rows.add(new String[]{"Net from Operating Activities", fmt(r.netOperating()), "H"});
        rows.add(new String[]{});
        rows.add(new String[]{"INVESTING ACTIVITIES", "", "H"});
        for (CashFlowReport.ActivityItem item : r.investingActivities()) {
            rows.add(new String[]{"  " + item.label(), fmt(item.amount())});
        }
        rows.add(new String[]{"Net from Investing Activities", fmt(r.netInvesting()), "H"});
        rows.add(new String[]{});
        rows.add(new String[]{"FINANCING ACTIVITIES", "", "H"});
        for (CashFlowReport.ActivityItem item : r.financingActivities()) {
            rows.add(new String[]{"  " + item.label(), fmt(item.amount())});
        }
        rows.add(new String[]{"Net from Financing Activities", fmt(r.netFinancing()), "H"});
        rows.add(new String[]{});
        rows.add(new String[]{"Net Cash Flow", fmt(r.netCashFlow()), "H"});
        rows.add(new String[]{"Closing Balance", fmt(r.closingBalance()), "H"});
        return rows;
    }

    private List<String[]> fpRows(FinancialPositionReport r) {
        return List.of(
                new String[]{"STATEMENT OF FINANCIAL POSITION", "", "H"},
                new String[]{"As at: " + r.asAtDate(), ""},
                new String[]{},
                new String[]{"ASSETS", "", "H"},
                new String[]{"  Cash and Cash Equivalents", fmt(r.cashAndEquivalents())},
                new String[]{"Total Assets", fmt(r.totalAssets()), "H"},
                new String[]{},
                new String[]{"LIABILITIES", "", "H"},
                new String[]{"  Outstanding Loans", fmt(r.outstandingLoans())},
                new String[]{"  Outstanding Debts", fmt(r.outstandingDebts())},
                new String[]{"Total Liabilities", fmt(r.totalLiabilities()), "H"},
                new String[]{},
                new String[]{"NET ASSETS", fmt(r.netAssets()), "H"},
                new String[]{},
                new String[]{"REPRESENTED BY", "", "H"},
                new String[]{"  Initial Capital", fmt(r.initialCapital())},
                new String[]{"  Accumulated Surplus/(Deficit)", fmt(r.accumulatedSurplusDeficit())},
                new String[]{"Total", fmt(r.netAssets()), "H"}
        );
    }

    private String fmt(BigDecimal value) {
        if (value == null) return "0.00";
        return String.format("%,.2f", value);
    }

    private String formatReportTitle(ReportType type) {
        return type.name().replace("_", " ");
    }

    private String buildFileName(String projectName, ReportType reportType, LocalDate start, LocalDate end, String ext) {
        String safeName = projectName.replaceAll("[^a-zA-Z0-9]", "_").toLowerCase();
        return safeName + "_" + reportType.name().toLowerCase() + "_" + start + "_to_" + end + "." + ext;
    }

    private GeneratedFileResponse toResponse(GeneratedFile f) {
        return new GeneratedFileResponse(f.getId(), f.getProject().getId(), f.getFileName(),
                f.getFileType().name(), f.getReportType().name(), f.getSizeBytes(), f.getCreatedAt());
    }
}
