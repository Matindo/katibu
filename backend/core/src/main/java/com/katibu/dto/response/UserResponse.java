package com.katibu.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(UUID id, String email, String fullName, LocalDateTime createdAt) {}
