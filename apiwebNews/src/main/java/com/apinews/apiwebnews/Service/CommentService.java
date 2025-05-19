package com.apinews.apiwebnews.Service;

import com.apinews.apiwebnews.Dto.Request.CommentRequest;
import com.apinews.apiwebnews.Dto.Response.CommentResponse;

import java.util.List;

public interface CommentService {
    CommentResponse createComment(CommentRequest request, Long userId);
    CommentResponse updateComment(Long commentId, CommentRequest request, Long userId);
    void deleteComment(Long commentId, Long userId);
    List<CommentResponse> getCommentsByNews(Long newsId);
    List<CommentResponse> getAllComments(); // 🔥 Thêm phương thức lấy tất cả bình luận

    // Kiểm tra xem user có phải là ADMIN không
    boolean isAdmin(Long userId);
}
