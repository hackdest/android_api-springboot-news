package com.apinews.apiwebnews.Dto.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewsRequest {
    @NotBlank(message = "TITLE_CANNOT_BE_EMPTY")
    @Size(min = 5, max = 255, message = "TITLE_LENGTH_INVALID")
    String title;

    @NotBlank(message = "CONTENT_CANNOT_BE_EMPTY")
    String content;

    MultipartFile image;
//    String image;
    Long categoryId;
}
