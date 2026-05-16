package com.katibu.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record GeneratedFileResponse(
        UUID id,
        UUID projectId,
        String fileName,
        String fileType,
        String reportType,
        Long sizeBytes,
        LocalDateTime createdAt
) {}
