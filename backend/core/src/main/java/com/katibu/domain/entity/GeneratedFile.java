package com.katibu.domain.entity;

import com.katibu.domain.enums.FileType;
import com.katibu.domain.enums.ReportType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "generated_files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneratedFile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false, length = 255)
    private String fileName;

    @Column(nullable = false, length = 500)
    private String objectKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private FileType fileType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ReportType reportType;

    private Long sizeBytes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onPersist() {
        createdAt = LocalDateTime.now();
    }
}
