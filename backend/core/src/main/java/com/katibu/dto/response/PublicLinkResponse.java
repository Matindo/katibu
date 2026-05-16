package com.katibu.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record PublicLinkResponse(
        UUID id,
        String token,
        String url,
        LocalDateTime expiresAt,
        boolean active,
        LocalDateTime createdAt
) {}
