package com.katibu.controller;

import com.katibu.dto.request.CreateEntryRequest;
import com.katibu.dto.request.UpdateEntryRequest;
import com.katibu.dto.response.ApiResponse;
import com.katibu.dto.response.LedgerEntryResponse;
import com.katibu.service.LedgerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/projects/{projectId}/entries")
@RequiredArgsConstructor
public class LedgerController {

    private final LedgerService ledgerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<LedgerEntryResponse> create(@PathVariable UUID projectId,
                                                   @Valid @RequestBody CreateEntryRequest req,
                                                   Authentication auth) {
        return ApiResponse.ok(ledgerService.create(projectId, req, auth.getName()), "Transaction recorded");
    }

    @GetMapping
    public ApiResponse<List<LedgerEntryResponse>> list(@PathVariable UUID projectId, Authentication auth) {
        return ApiResponse.ok(ledgerService.list(projectId, auth.getName()));
    }

    @GetMapping("/{entryId}")
    public ApiResponse<LedgerEntryResponse> get(@PathVariable UUID projectId,
                                                @PathVariable UUID entryId,
                                                Authentication auth) {
        return ApiResponse.ok(ledgerService.get(projectId, entryId, auth.getName()));
    }

    @PutMapping("/{entryId}")
    public ApiResponse<LedgerEntryResponse> update(@PathVariable UUID projectId,
                                                   @PathVariable UUID entryId,
                                                   @Valid @RequestBody UpdateEntryRequest req,
                                                   Authentication auth) {
        return ApiResponse.ok(ledgerService.update(projectId, entryId, req, auth.getName()), "Transaction updated");
    }

    @DeleteMapping("/{entryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID projectId, @PathVariable UUID entryId, Authentication auth) {
        ledgerService.delete(projectId, entryId, auth.getName());
    }
}
