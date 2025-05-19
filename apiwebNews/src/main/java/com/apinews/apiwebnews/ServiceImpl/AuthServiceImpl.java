package com.apinews.apiwebnews.ServiceImpl;

import com.apinews.apiwebnews.Config.EmailService;
import com.apinews.apiwebnews.Config.JwtTokenProvider;
import com.apinews.apiwebnews.Dto.*;
import com.apinews.apiwebnews.Dto.Request.RefreshTokenRequest;
import com.apinews.apiwebnews.Model.RefreshToken;
import com.apinews.apiwebnews.Model.Role;
import com.apinews.apiwebnews.Model.User;
import com.apinews.apiwebnews.Repository.UserRepository;
import com.apinews.apiwebnews.Service.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final EmailService emailService; // Giả sử bạn có dịch vụ gửi email
    // 📌 Quên mật khẩu - Gửi email reset
    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Xóa token cũ trước khi tạo token mới
        user.setResetToken(null);

        // Tạo token reset mới
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);


        // Tạo mật khẩu ngẫu nhiên 8 ký tự (chỉ bao gồm số)
        String newPassword = generateRandomPassword(8);
        // Cập nhật mật khẩu mới cho người dùng
        user.setPassword(passwordEncoder.encode(newPassword));
        // Thêm thời gian hết hạn token (VD: 15 phút)


        userRepository.save(user);

        // Gửi email với link reset (Dùng API backend chứ không phải FE)
        String resetLink = "http://localhost:3000/api/auth/reset-password?token=" + token+ "mật khẩu ramdom "+ newPassword;
        emailService.sendEmail(user.getUsername(), "Reset Password",
                "Click vào link để đặt lại mật khẩu (có hiệu lực trong 15 phút): " + resetLink);
    }

    // Hàm tạo mật khẩu ngẫu nhiên 8 ký tự (chỉ bao gồm số)
    private String generateRandomPassword(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            password.append(random.nextInt(10));  // Chỉ lấy số từ 0 đến 9
        }
        return password.toString();
    }
    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByResetToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (request.getNewPassword() == null || request.getNewPassword().length() < 6) {
            throw new RuntimeException("Password must be at least 6 characters long");
        }
        System.out.println("User found: " + user.getUsername());
        System.out.println("Received token: " + request.getToken());

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetToken(null);
        userRepository.save(user);
    }


    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists!");
        }

        Role role = request.getRole() != null ? request.getRole() : Role.USER;

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        userRepository.save(user);

        // 🔥 Gửi role khi tạo token
        String token = jwtTokenProvider.generateToken(user.getUsername(), user.getRole().name());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        return new AuthResponse(token, refreshToken.getToken());
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Sai tên đăng nhập hoặc mật khẩu!");
        } catch (DisabledException e) {
            throw new RuntimeException("Tài khoản bị vô hiệu hóa!");
        } catch (Exception e) {
            throw new RuntimeException("Đăng nhập thất bại!");
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại!"));

        // Kiểm tra nếu user đã có refresh token trước khi xóa
        refreshTokenService.deleteToken(user.getUsername());

        String token = jwtTokenProvider.generateToken(user.getUsername(), user.getRole().name());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        return new AuthResponse(token, refreshToken.getToken());
    }



    @Override
    public void logout(String refreshToken) {
        refreshTokenService.deleteToken(refreshToken);
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        return refreshTokenService.findByToken(request.getRefreshToken())
                .map(refreshToken -> {
                    if (refreshTokenService.isTokenExpired(refreshToken)) {
                        refreshTokenService.deleteToken(refreshToken.getToken());
                        throw new RuntimeException("Refresh token expired");
                    }
                    User user = refreshToken.getUser();
                    String token = jwtTokenProvider.generateToken(user.getUsername(), user.getRole().name());
                    return new AuthResponse(token, refreshToken.getToken());
                })
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
    }


}
