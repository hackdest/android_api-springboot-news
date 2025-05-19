package com.apinews.apiwebnews.ServiceImpl;

import com.apinews.apiwebnews.Dto.Request.CommentRequest;
import com.apinews.apiwebnews.Dto.Response.CommentResponse;
import com.apinews.apiwebnews.Exception.ResourceNotFoundException;
import com.apinews.apiwebnews.Model.Comment;
import com.apinews.apiwebnews.Model.News;
import com.apinews.apiwebnews.Model.Role;
import com.apinews.apiwebnews.Model.User;
import com.apinews.apiwebnews.Repository.CommentRepository;
import com.apinews.apiwebnews.Repository.NewsRepository;
import com.apinews.apiwebnews.Repository.UserRepository;
import com.apinews.apiwebnews.Service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final NewsRepository newsRepository;
    private final UserRepository userRepository;

    // 📌 Thêm bình luận mới vào một bài viết
    @Override
    public CommentResponse createComment(CommentRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        News news = newsRepository.findById(request.getNewsId())
                .orElseThrow(() -> new ResourceNotFoundException("News not found"));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .user(user)
                .news(news)
                .build();
        commentRepository.save(comment);
        return toCommentResponse(comment);
    }

    @Override
    public CommentResponse updateComment(Long commentId, CommentRequest request, Long userId) {
        try {
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

            // Kiểm tra nếu user là chủ comment hoặc là ADMIN
            if (!comment.getUser().getId().equals(userId) && !isAdmin(userId)) {
                throw new IllegalStateException("Unauthorized to update this comment");
            }

            comment.setContent(request.getContent());
            commentRepository.save(comment);
            return toCommentResponse(comment);
        } catch (Exception e) {
            // Log lỗi nếu có lỗi xảy ra
            e.printStackTrace();
            throw new RuntimeException("Error while updating comment: " + e.getMessage());
        }
    }


    // 📌 Xóa bình luận (chỉ người tạo hoặc ADMIN mới được xóa)
    @Override
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        // Kiểm tra nếu user là chủ comment hoặc là ADMIN
        if (!comment.getUser().getId().equals(userId) && !isAdmin(userId)) {
            throw new IllegalStateException("Unauthorized to delete this comment");
        }

        commentRepository.deleteById(commentId);
    }

    // 📌 Lấy danh sách bình luận theo bài viết
    @Override
    public List<CommentResponse> getCommentsByNews(Long newsId) {
        return commentRepository.findByNewsId(newsId)
                .stream()
                .map(this::toCommentResponse)
                .collect(Collectors.toList());
    }

    // 📌 Kiểm tra quyền ADMIN của user
    @Override
    public boolean isAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return user.getRole().equals(Role.ADMIN);
    }
    // 📌 Lấy tất cả bình luận
    @Override
    public List<CommentResponse> getAllComments() {
        return commentRepository.findAll()
                .stream()
                .map(this::toCommentResponse)
                .collect(Collectors.toList());
    }
    // 📌 Chuyển đổi Comment sang CommentResponse
    private CommentResponse toCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .username(comment.getUser().getUsername())
                .build();
    }
}
