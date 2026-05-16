package com.katibu.service;

import com.katibu.domain.entity.Project;
import com.katibu.domain.entity.ProjectMember;
import com.katibu.domain.entity.User;
import com.katibu.domain.enums.DurationType;
import com.katibu.domain.enums.MemberRole;
import com.katibu.domain.enums.ProjectStatus;
import com.katibu.dto.request.AddMemberRequest;
import com.katibu.dto.request.CreateProjectRequest;
import com.katibu.dto.request.UpdateProjectRequest;
import com.katibu.dto.response.MemberResponse;
import com.katibu.dto.response.ProjectResponse;
import com.katibu.exception.BusinessException;
import com.katibu.exception.ResourceNotFoundException;
import com.katibu.exception.UnauthorizedException;
import com.katibu.repository.ProjectMemberRepository;
import com.katibu.repository.ProjectRepository;
import com.katibu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository memberRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    @Transactional
    public ProjectResponse create(CreateProjectRequest req, String userEmail) {
        User creator = authService.requireByEmail(userEmail);
        LocalDate endDate = resolveEndDate(req.durationType(), req.startDate(), req.endDate());
        Project project = Project.builder()
                .name(req.name())
                .description(req.description())
                .creator(creator)
                .durationType(req.durationType())
                .startDate(req.startDate())
                .endDate(endDate)
                .status(ProjectStatus.ACTIVE)
                .build();
        return toResponse(projectRepository.save(project));
    }

    @Transactional(readOnly = true)
    public ProjectResponse get(UUID projectId, String userEmail) {
        Project project = requireAccessible(projectId, userEmail);
        return toResponse(project);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> listForUser(String userEmail) {
        User user = authService.requireByEmail(userEmail);
        return projectRepository.findAccessibleByUserId(user.getId()).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ProjectResponse update(UUID projectId, UpdateProjectRequest req, String userEmail) {
        Project project = requireAccessible(projectId, userEmail);
        if (project.getStatus() == ProjectStatus.ARCHIVED) {
            throw new BusinessException("Cannot modify an archived project");
        }
        project.setName(req.name());
        project.setDescription(req.description());
        return toResponse(projectRepository.save(project));
    }

    @Transactional
    public ProjectResponse archive(UUID projectId, String userEmail) {
        Project project = requireCreator(projectId, userEmail);
        if (project.getStatus() == ProjectStatus.ARCHIVED) {
            throw new BusinessException("Project is already archived");
        }
        project.setStatus(ProjectStatus.ARCHIVED);
        return toResponse(projectRepository.save(project));
    }

    @Transactional
    public MemberResponse addMember(UUID projectId, AddMemberRequest req, String userEmail) {
        Project project = requireCreator(projectId, userEmail);
        User newMember = userRepository.findByEmail(req.email().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + req.email()));
        if (newMember.getEmail().equals(project.getCreator().getEmail())) {
            throw new BusinessException("Project creator cannot be added as a member");
        }
        if (memberRepository.existsByProjectIdAndUserId(projectId, newMember.getId())) {
            throw new BusinessException("User is already a member of this project");
        }
        User actor = authService.requireByEmail(userEmail);
        ProjectMember member = ProjectMember.builder()
                .project(project)
                .user(newMember)
                .role(MemberRole.ADMIN)
                .addedBy(actor)
                .build();
        member = memberRepository.save(member);
        return toMemberResponse(member);
    }

    @Transactional
    public void removeMember(UUID projectId, UUID userId, String userEmail) {
        requireCreator(projectId, userEmail);
        if (!memberRepository.existsByProjectIdAndUserId(projectId, userId)) {
            throw new ResourceNotFoundException("Member not found in this project");
        }
        memberRepository.deleteByProjectIdAndUserId(projectId, userId);
    }

    @Transactional(readOnly = true)
    public List<MemberResponse> listMembers(UUID projectId, String userEmail) {
        requireAccessible(projectId, userEmail);
        return memberRepository.findByProjectIdOrderByAddedAtAsc(projectId).stream()
                .map(this::toMemberResponse)
                .toList();
    }

    public Project requireAccessible(UUID projectId, String userEmail) {
        User user = authService.requireByEmail(userEmail);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        boolean isCreator = project.getCreator().getId().equals(user.getId());
        boolean isMember = memberRepository.existsByProjectIdAndUserId(projectId, user.getId());
        if (!isCreator && !isMember) {
            throw new UnauthorizedException("Access denied to this project");
        }
        return project;
    }

    public Project requireCreator(UUID projectId, String userEmail) {
        User user = authService.requireByEmail(userEmail);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        if (!project.getCreator().getId().equals(user.getId())) {
            throw new UnauthorizedException("Only the project creator can perform this action");
        }
        return project;
    }

    private LocalDate resolveEndDate(DurationType type, LocalDate startDate, LocalDate provided) {
        return switch (type) {
            case WEEKLY -> startDate.plusWeeks(1);
            case MONTHLY -> startDate.plusMonths(1);
            case QUARTERLY -> startDate.plusMonths(3);
            case HALF_YEARLY -> startDate.plusMonths(6);
            case YEARLY -> startDate.plusYears(1);
            case CUSTOM -> {
                if (provided == null) throw new BusinessException("End date is required for CUSTOM duration");
                if (!provided.isAfter(startDate)) throw new BusinessException("End date must be after start date");
                yield provided;
            }
        };
    }

    private ProjectResponse toResponse(Project p) {
        return new ProjectResponse(
                p.getId(), p.getName(), p.getDescription(),
                p.getDurationType().name(), p.getStartDate(), p.getEndDate(),
                p.getStatus().name(),
                p.getCreator().getId(), p.getCreator().getFullName(),
                p.getCreatedAt(), p.getUpdatedAt());
    }

    private MemberResponse toMemberResponse(ProjectMember m) {
        return new MemberResponse(
                m.getId(), m.getUser().getId(),
                m.getUser().getEmail(), m.getUser().getFullName(),
                m.getRole().name(), m.getAddedAt());
    }
}
