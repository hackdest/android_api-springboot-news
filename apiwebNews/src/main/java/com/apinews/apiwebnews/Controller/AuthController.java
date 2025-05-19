package com.apinews.apiwebnews.Controller;

import com.apinews.apiwebnews.Dto.*;
import com.apinews.apiwebnews.Dto.Request.RefreshTokenRequest;
import com.apinews.apiwebnews.Model.RefreshToken;
import com.apinews.apiwebnews.Service.AuthService;
import com.apinews.apiwebnews.ServiceImpl.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register( @Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login( @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/logout")
    @Transactional
    public ResponseEntity<String> logout(@RequestBody RefreshTokenRequest request) {
        Optional<RefreshToken> refreshToken = refreshTokenService.findByToken(request.getRefreshToken());

        if (refreshToken.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        refreshTokenService.deleteToken(request.getRefreshToken());
        return ResponseEntity.ok("Logged out successfully");
    }


    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        try {
            authService.forgotPassword(request);
            return ResponseEntity.ok("Email đặt lại mật khẩu đã được gửi!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi gửi email đặt lại mật khẩu: " + e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            authService.resetPassword(request);
            return ResponseEntity.ok("Mật khẩu đã được đặt lại thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi đặt lại mật khẩu: " + e.getMessage());
        }
    }
}
