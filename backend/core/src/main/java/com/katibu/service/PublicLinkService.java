package com.katibu.service;

import com.katibu.domain.entity.Project;
import com.katibu.domain.entity.PublicLink;
import com.katibu.domain.entity.User;
import com.katibu.dto.request.GenerateLinkRequest;
import com.katibu.dto.response.PublicLinkResponse;
import com.katibu.exception.BusinessException;
import com.katibu.exception.ResourceNotFoundException;
import com.katibu.repository.PublicLinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PublicLinkService {

    private final PublicLinkRepository linkRepository;
    private final ProjectService projectService;
    private final AuthService authService;

    @Value("${app.frontend-base-url}")
    private String frontendBaseUrl;

    @Transactional
    public PublicLinkResponse generate(UUID projectId, GenerateLinkRequest req, String userEmail) {
        Project project = projectService.requireAccessible(projectId, userEmail);
        User actor = authService.requireByEmail(userEmail);
        PublicLink link = PublicLink.builder()
                .project(project)
                .createdBy(actor)
                .expiresAt(req != null ? req.expiresAt() : null)
                .build();
        link = linkRepository.save(link);
        return toResponse(link);
    }

    @Transactional(readOnly = true)
    public List<PublicLinkResponse> list(UUID projectId, String userEmail) {
        projectService.requireAccessible(projectId, userEmail);
        return linkRepository.findByProjectIdOrderByCreatedAtDesc(projectId).stream()
                .map(this::toResponse).toList();
    }

    @Transactional
    public void revoke(UUID projectId, UUID linkId, String userEmail) {
        projectService.requireAccessible(projectId, userEmail);
        PublicLink link = linkRepository.findByIdAndProjectId(linkId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Public link not found"));
        if (!link.isActive()) {
            throw new BusinessException("Link is already revoked");
        }
        link.setActive(false);
        linkRepository.save(link);
    }

    @Transactional(readOnly = true)
    public Project resolveToken(String token) {
        PublicLink link = linkRepository.findByTokenAndActiveTrue(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid or expired link"));
        if (link.getExpiresAt() != null && link.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException("This link has expired");
        }
        return link.getProject();
    }

    private PublicLinkResponse toResponse(PublicLink link) {
        String url = frontendBaseUrl + "/public/" + link.getToken();
        return new PublicLinkResponse(link.getId(), link.getToken(), url,
                link.getExpiresAt(), link.isActive(), link.getCreatedAt());
    }
}
