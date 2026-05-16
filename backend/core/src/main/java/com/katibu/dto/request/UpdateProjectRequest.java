package com.katibu.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateProjectRequest(
        @NotBlank String name,
        String description
) {}
