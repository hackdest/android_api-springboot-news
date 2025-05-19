package com.apinews.apiwebnews.Dto.Request;

import com.apinews.apiwebnews.Model.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String password;
    String firstName;
    String lastName;
    Role role; // Dùng Enum thay vì List<String>
}
