package com.katibu.service;

import com.katibu.domain.entity.User;
import com.katibu.dto.request.ChangePasswordRequest;
import com.katibu.dto.request.LoginRequest;
import com.katibu.dto.request.RegisterRequest;
import com.katibu.dto.request.UpdateProfileRequest;
import com.katibu.dto.response.AuthResponse;
import com.katibu.dto.response.UserResponse;
import com.katibu.exception.BusinessException;
import com.katibu.exception.ResourceNotFoundException;
import com.katibu.repository.UserRepository;
import com.katibu.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email already registered");
        }
        User user = User.builder()
                .email(request.email().toLowerCase())
                .passwordHash(passwordEncoder.encode(request.password()))
                .fullName(request.fullName())
                .build();
        user = userRepository.save(user);
        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user.getId(), user.getEmail(), user.getFullName());
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid credentials"));
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BusinessException("Invalid credentials");
        }
        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user.getId(), user.getEmail(), user.getFullName());
    }

    @Transactional(readOnly = true)
    public User requireByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    public UserResponse getProfile(String email) {
        User user = requireByEmail(email);
        return new UserResponse(user.getId(), user.getEmail(), user.getFullName(), user.getCreatedAt());
    }

    @Transactional
    public UserResponse updateProfile(String email, UpdateProfileRequest request) {
        User user = requireByEmail(email);
        String newEmail = request.email().toLowerCase();
        if (!newEmail.equals(user.getEmail()) && userRepository.existsByEmail(newEmail)) {
            throw new BusinessException("Email already in use");
        }
        user.setEmail(newEmail);
        user.setFullName(request.fullName());
        user = userRepository.save(user);
        return new UserResponse(user.getId(), user.getEmail(), user.getFullName(), user.getCreatedAt());
    }

    @Transactional
    public void changePassword(String email, ChangePasswordRequest request) {
        User user = requireByEmail(email);
        if (!passwordEncoder.matches(request.currentPassword(), user.getPasswordHash())) {
            throw new BusinessException("Current password is incorrect");
        }
        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }
}
