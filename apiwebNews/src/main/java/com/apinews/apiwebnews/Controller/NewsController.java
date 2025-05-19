package com.apinews.apiwebnews.Controller;

import com.apinews.apiwebnews.Dto.ApiResponse;
import com.apinews.apiwebnews.Dto.Request.NewsRequest;
import com.apinews.apiwebnews.Dto.Response.NewsResponse;
import com.apinews.apiwebnews.Service.NewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;

    @GetMapping
    public ResponseEntity<Page<NewsResponse>> getAllNews(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(newsService.getAllNews(page - 1, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsResponse> getNewsById(@PathVariable Long id) {
        return ResponseEntity.ok(newsService.getNewsById(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> createNews(@Valid @ModelAttribute NewsRequest request) {
        return ResponseEntity.ok(newsService.createNews(request));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> updateNews(@PathVariable Long id,
                                                  @Valid @ModelAttribute NewsRequest request) {
        return ResponseEntity.ok(newsService.updateNews(id, request));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteNews(@PathVariable Long id) {
        return ResponseEntity.ok(newsService.deleteNews(id));
    }
}
