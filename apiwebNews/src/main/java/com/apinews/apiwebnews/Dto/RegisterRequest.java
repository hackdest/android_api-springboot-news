package com.apinews.apiwebnews.Dto;

import com.apinews.apiwebnews.Model.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class    RegisterRequest {
    @NotBlank(message = "USERNAME_REQUIRED")
    private String username;

    @NotBlank(message = "PASSWORD_REQUIRED")
    private String password;

    private Role role = Role.USER;  // Mặc định là USER nếu không chọn
}
