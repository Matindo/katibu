package com.katibu.controller;

import com.katibu.dto.request.ChangePasswordRequest;
import com.katibu.dto.request.UpdateProfileRequest;
import com.katibu.dto.response.ApiResponse;
import com.katibu.dto.response.UserResponse;
import com.katibu.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;

    @GetMapping("/me")
    public ApiResponse<UserResponse> getProfile(Principal principal) {
        return ApiResponse.ok(authService.getProfile(principal.getName()));
    }

    @PutMapping("/me")
    public ApiResponse<UserResponse> updateProfile(Principal principal,
                                                    @Valid @RequestBody UpdateProfileRequest request) {
        return ApiResponse.ok(authService.updateProfile(principal.getName(), request));
    }

    @PutMapping("/me/password")
    public ApiResponse<Void> changePassword(Principal principal,
                                             @Valid @RequestBody ChangePasswordRequest request) {
        authService.changePassword(principal.getName(), request);
        return ApiResponse.ok(null);
    }
}
