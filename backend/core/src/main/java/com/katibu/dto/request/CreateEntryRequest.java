package com.katibu.dto.request;

import com.katibu.domain.enums.EntryType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateEntryRequest(
        @NotNull EntryType entryType,
        @NotNull @DecimalMin(value = "0.01", message = "Amount must be greater than zero") BigDecimal amount,
        @NotBlank String description,
        String reference,
        @NotNull LocalDate transactionDate
) {}
