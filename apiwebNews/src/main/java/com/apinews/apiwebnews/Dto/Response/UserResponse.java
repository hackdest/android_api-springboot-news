package com.apinews.apiwebnews.Dto.Response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String role;  // 🔥 Dùng String thay vì Set<Role>
}
