package com.katibu.controller;

import com.katibu.dto.request.AddMemberRequest;
import com.katibu.dto.request.CreateProjectRequest;
import com.katibu.dto.request.UpdateProjectRequest;
import com.katibu.dto.response.ApiResponse;
import com.katibu.dto.response.MemberResponse;
import com.katibu.dto.response.ProjectResponse;
import com.katibu.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ProjectResponse> create(@Valid @RequestBody CreateProjectRequest req, Authentication auth) {
        return ApiResponse.ok(projectService.create(req, auth.getName()));
    }

    @GetMapping
    public ApiResponse<List<ProjectResponse>> list(Authentication auth) {
        return ApiResponse.ok(projectService.listForUser(auth.getName()));
    }

    @GetMapping("/{id}")
    public ApiResponse<ProjectResponse> get(@PathVariable UUID id, Authentication auth) {
        return ApiResponse.ok(projectService.get(id, auth.getName()));
    }

    @PutMapping("/{id}")
    public ApiResponse<ProjectResponse> update(@PathVariable UUID id,
                                               @Valid @RequestBody UpdateProjectRequest req,
                                               Authentication auth) {
        return ApiResponse.ok(projectService.update(id, req, auth.getName()));
    }

    @PostMapping("/{id}/archive")
    public ApiResponse<ProjectResponse> archive(@PathVariable UUID id, Authentication auth) {
        return ApiResponse.ok(projectService.archive(id, auth.getName()));
    }

    @GetMapping("/{id}/members")
    public ApiResponse<List<MemberResponse>> listMembers(@PathVariable UUID id, Authentication auth) {
        return ApiResponse.ok(projectService.listMembers(id, auth.getName()));
    }

    @PostMapping("/{id}/members")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<MemberResponse> addMember(@PathVariable UUID id,
                                                 @Valid @RequestBody AddMemberRequest req,
                                                 Authentication auth) {
        return ApiResponse.ok(projectService.addMember(id, req, auth.getName()));
    }

    @DeleteMapping("/{id}/members/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeMember(@PathVariable UUID id, @PathVariable UUID userId, Authentication auth) {
        projectService.removeMember(id, userId, auth.getName());
    }
}
