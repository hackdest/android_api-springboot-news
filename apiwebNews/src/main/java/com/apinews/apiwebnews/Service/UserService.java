package com.apinews.apiwebnews.Service;

import com.apinews.apiwebnews.Dto.Request.UserCreationRequest;
import com.apinews.apiwebnews.Dto.Request.UserUpdateRequest;
import com.apinews.apiwebnews.Dto.Response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserCreationRequest request);
    UserResponse updateUser(Long userId, UserUpdateRequest request);
    UserResponse getUserById(Long userId);
    UserResponse getUserByUsername(String username);  // 📌 Thêm phương thức lấy thông tin theo username
    void deleteUser(Long userId);
    List<UserResponse> getAllUsers();
}
