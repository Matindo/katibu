package com.katibu.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record MemberResponse(UUID id, UUID userId, String email, String fullName, String role, LocalDateTime addedAt) {}
