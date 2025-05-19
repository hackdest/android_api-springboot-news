package com.apinews.apiwebnews.Service;

import com.apinews.apiwebnews.Dto.ApiResponse;
import com.apinews.apiwebnews.Dto.Request.NewsRequest;
import com.apinews.apiwebnews.Dto.Response.NewsResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface NewsService {
    NewsResponse getNewsById(Long newsId);
    List<NewsResponse> getAllNews();
    List<NewsResponse> getNewsByCategory(Long categoryId);

    Page<NewsResponse> getAllNews(int page, int size);
    ApiResponse createNews(NewsRequest request);
    ApiResponse updateNews(Long id, NewsRequest request);
    ApiResponse deleteNews(Long id);


}
