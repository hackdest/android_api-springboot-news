package com.apinews.apiwebnews.Controller;

import com.apinews.apiwebnews.Dto.Request.UserUpdateRequest;
import com.apinews.apiwebnews.Dto.Request.UserCreationRequest;
import com.apinews.apiwebnews.Dto.Response.UserResponse;
import com.apinews.apiwebnews.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 📌 Lấy thông tin người dùng hiện tại
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.getUserByUsername(userDetails.getUsername()));
    }

    // 📌 Cập nhật thông tin người dùng hiện tại
    @PutMapping("/update")
    public ResponseEntity<UserResponse> updateCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UserUpdateRequest userUpdateRequest) {
        UserResponse userResponse = userService.getUserByUsername(userDetails.getUsername());
        return ResponseEntity.ok(userService.updateUser(userResponse.getId(), userUpdateRequest));
    }

    // 📌 Lấy danh sách tất cả người dùng (Chỉ Admin)
    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // 📌 Lấy thông tin một người dùng theo ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // 📌 Tạo người dùng mới (Chỉ Admin)
    @PostMapping("/create")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserCreationRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    // 📌 Cập nhật thông tin người dùng theo ID (Chỉ Admin)
    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponse> updateUserById(
            @PathVariable Long id,
            @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    // 📌 Xóa người dùng theo ID (Chỉ Admin)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
