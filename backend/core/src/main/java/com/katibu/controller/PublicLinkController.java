package com.katibu.controller;

import com.katibu.dto.request.GenerateLinkRequest;
import com.katibu.dto.response.ApiResponse;
import com.katibu.dto.response.PublicLinkResponse;
import com.katibu.service.PublicLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/projects/{projectId}/links")
@RequiredArgsConstructor
public class PublicLinkController {

    private final PublicLinkService publicLinkService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PublicLinkResponse> generate(@PathVariable UUID projectId,
                                                    @RequestBody(required = false) GenerateLinkRequest req,
                                                    Authentication auth) {
        return ApiResponse.ok(publicLinkService.generate(projectId, req, auth.getName()), "Public link generated");
    }

    @GetMapping
    public ApiResponse<List<PublicLinkResponse>> list(@PathVariable UUID projectId, Authentication auth) {
        return ApiResponse.ok(publicLinkService.list(projectId, auth.getName()));
    }

    @DeleteMapping("/{linkId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void revoke(@PathVariable UUID projectId, @PathVariable UUID linkId, Authentication auth) {
        publicLinkService.revoke(projectId, linkId, auth.getName());
    }
}
