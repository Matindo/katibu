package com.katibu.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProjectResponse(
        UUID id,
        String name,
        String description,
        String durationType,
        LocalDate startDate,
        LocalDate endDate,
        String status,
        UUID creatorId,
        String creatorName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
