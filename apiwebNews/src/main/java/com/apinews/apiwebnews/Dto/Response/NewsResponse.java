package com.apinews.apiwebnews.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsResponse {
    private Long id;
    private String title;
    private String content;
    private String imageUrl; // Thêm trường ảnh

    private String categoryName; // 🔥 Thay categoryId bằng categoryName
}
