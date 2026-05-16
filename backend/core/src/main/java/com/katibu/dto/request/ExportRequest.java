package com.katibu.dto.request;

import com.katibu.domain.enums.FileType;
import com.katibu.domain.enums.ReportType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ExportRequest(
        @NotNull FileType fileType,
        @NotNull ReportType reportType,
        LocalDate startDate,
        LocalDate endDate
) {}
