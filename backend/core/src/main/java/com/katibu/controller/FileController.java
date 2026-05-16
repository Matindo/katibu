package com.katibu.controller;

import com.katibu.domain.entity.GeneratedFile;
import com.katibu.dto.request.ExportRequest;
import com.katibu.dto.response.ApiResponse;
import com.katibu.dto.response.GeneratedFileResponse;
import com.katibu.service.FileExportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/projects/{projectId}/files")
@RequiredArgsConstructor
public class FileController {

    private final FileExportService fileExportService;

    @PostMapping("/export")
    public ApiResponse<GeneratedFileResponse> export(@PathVariable UUID projectId,
                                                     @Valid @RequestBody ExportRequest req,
                                                     Authentication auth) {
        return ApiResponse.ok(fileExportService.export(projectId, req, auth.getName()));
    }

    @GetMapping
    public ApiResponse<List<GeneratedFileResponse>> list(@PathVariable UUID projectId, Authentication auth) {
        return ApiResponse.ok(fileExportService.list(projectId, auth.getName()));
    }

    @GetMapping("/{fileId}/download")
    public ResponseEntity<byte[]> download(@PathVariable UUID projectId,
                                           @PathVariable UUID fileId,
                                           Authentication auth) {
        GeneratedFile file = fileExportService.getFile(projectId, fileId, auth.getName());
        byte[] data = fileExportService.download(projectId, fileId, auth.getName());

        String contentType = file.getFileType().name().equals("PDF") ? "application/pdf" : "text/csv";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .contentLength(data.length)
                .body(data);
    }
}
