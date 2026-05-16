package com.katibu.dto.request;

import com.katibu.domain.enums.DurationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateProjectRequest(
        @NotBlank String name,
        String description,
        @NotNull DurationType durationType,
        @NotNull LocalDate startDate,
        LocalDate endDate
) {}
