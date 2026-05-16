package com.katibu.dto.request;

import java.time.LocalDateTime;

public record GenerateLinkRequest(LocalDateTime expiresAt) {}
