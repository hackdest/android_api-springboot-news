package com.apinews.apiwebnews.Service;


import com.apinews.apiwebnews.Dto.*;
import com.apinews.apiwebnews.Dto.Request.RefreshTokenRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    void logout(String refreshToken);
    AuthResponse refreshToken(RefreshTokenRequest request); // Sửa lại kiểu dữ liệu
    void forgotPassword(ForgotPasswordRequest request);
    void resetPassword(ResetPasswordRequest request);
}
