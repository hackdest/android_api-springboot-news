package com.apinews.apiwebnews.Controller;

import com.apinews.apiwebnews.Dto.Request.CategoryRequest;
import com.apinews.apiwebnews.Dto.Response.CategoryResponse;
import com.apinews.apiwebnews.Dto.Response.NewsResponse;
import com.apinews.apiwebnews.Service.CategoryService;
import com.apinews.apiwebnews.Service.NewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
@Validated
public class CategoryController {

    private final CategoryService categoryService;
    private final NewsService newsService;

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody @Valid CategoryRequest request) {
        return ResponseEntity.ok(categoryService.createCategory(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long id, @RequestBody @Valid CategoryRequest request) {
        return ResponseEntity.ok(categoryService.updateCategory(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    // ✅ Tránh trùng lặp đường dẫn bằng cách đặt rõ categoryId
    @GetMapping("/{categoryId}/news")
    public ResponseEntity<List<NewsResponse>> getNewsByCategory(@PathVariable Long categoryId) {
        List<NewsResponse> newsList = newsService.getNewsByCategory(categoryId);
        return newsList.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(newsList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}
