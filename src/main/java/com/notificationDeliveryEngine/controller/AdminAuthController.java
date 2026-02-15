package com.notificationDeliveryEngine.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.notificationDeliveryEngine.config.JwtUtil;
import com.notificationDeliveryEngine.dto.AuthRequestDTO;
import com.notificationDeliveryEngine.dto.AuthResponseDTO;
import com.notificationDeliveryEngine.repository.AdminRepository;

@RestController
@RequestMapping("/admin/auth")
@RequiredArgsConstructor
@Slf4j
public class AdminAuthController {

    private final AdminRepository adminRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder; // ✅ ADD THIS

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody AuthRequestDTO request) {

        log.info("Admin login attempt: {}", request.getUsername());

        var admin = adminRepository
                .findByAdminUserName(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username"));

        // ✅ CORRECT BCrypt validation
        if (!passwordEncoder.matches(request.getPassword(), admin.getAdminPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(admin.getAdminUserName());
        return new AuthResponseDTO(token);
    }
}