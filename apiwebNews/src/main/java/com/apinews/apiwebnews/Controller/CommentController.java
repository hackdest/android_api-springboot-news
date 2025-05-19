package com.apinews.apiwebnews.Controller;

import com.apinews.apiwebnews.Dto.Request.CommentRequest;
import com.apinews.apiwebnews.Dto.Response.CommentResponse;
import com.apinews.apiwebnews.Exception.ResourceNotFoundException;
import com.apinews.apiwebnews.Model.User;
import com.apinews.apiwebnews.Repository.UserRepository;
import com.apinews.apiwebnews.Service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final UserRepository userRepository;
    // 📌 Lấy danh sách tất cả bình luận
    @GetMapping
    public ResponseEntity<List<CommentResponse>> getAllComments() {
        return ResponseEntity.ok(commentService.getAllComments());
    }
    // 📌 Lấy danh sách bình luận theo bài viết
    @GetMapping("/{newsId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByNews(@PathVariable Long newsId) {
        return ResponseEntity.ok(commentService.getCommentsByNews(newsId));
    }

    // 📌 Thêm bình luận mới
    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CommentRequest request) {
        // 🔥 Tìm User theo username
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return ResponseEntity.ok(commentService.createComment(request, user.getId()));
    }

    // 📌 Cập nhật bình luận
    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> updateComment(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @RequestBody CommentRequest request) {
        // 🔥 Tìm User theo username
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return ResponseEntity.ok(commentService.updateComment(id, request, user.getId()));
    }

    // 📌 Xóa bình luận
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        // 🔥 Tìm User theo username
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        commentService.deleteComment(id, user.getId());
        return ResponseEntity.ok("Deleted successfully");
    }
}
