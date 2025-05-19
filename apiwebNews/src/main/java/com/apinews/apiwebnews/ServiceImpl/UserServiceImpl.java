package com.apinews.apiwebnews.ServiceImpl;

import com.apinews.apiwebnews.Dto.Request.UserCreationRequest;
import com.apinews.apiwebnews.Dto.Request.UserUpdateRequest;
import com.apinews.apiwebnews.Dto.Response.UserResponse;
import com.apinews.apiwebnews.Exception.ResourceNotFoundException;
import com.apinews.apiwebnews.Model.Role;
import com.apinews.apiwebnews.Model.User;
import com.apinews.apiwebnews.Repository.UserRepository;
import com.apinews.apiwebnews.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createUser(UserCreationRequest request) {
        // Nếu request.getRole() null, mặc định là USER
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists!");
        }
        Role role = (request.getRole() != null) ? request.getRole() : Role.USER;

        User user = User.builder()
                .username(request.getUsername())
                .password(request.getPassword() != null ? passwordEncoder.encode(request.getPassword()) : null)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(role)
                .build();

        userRepository.save(user);
        return toUserResponse(user);
    }
    @Override
    public UserResponse updateUser(Long userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());


        // Cập nhật role nếu request có giá trị hợp lệ
        if (request.getRole() != null) {
            user.setRole(request.getRole());  // Gán trực tiếp Enum
        }

        userRepository.save(user);
        return toUserResponse(user);
    }


    @Override
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return toUserResponse(user);
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return toUserResponse(user);
    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(userId);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());
    }

    private UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().name()) // 🔥 Chỉ lấy String từ Enum Role
                .build();
    }

}
