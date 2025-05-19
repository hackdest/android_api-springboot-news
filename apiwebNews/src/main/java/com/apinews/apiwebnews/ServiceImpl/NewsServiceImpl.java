//package com.apinews.apiwebnews.ServiceImpl;
//
//import com.apinews.apiwebnews.Dto.ApiResponse;
//import com.apinews.apiwebnews.Dto.Request.NewsRequest;
//import com.apinews.apiwebnews.Dto.Response.NewsResponse;
//import com.apinews.apiwebnews.Exception.ResourceNotFoundException;
//import com.apinews.apiwebnews.Model.Category;
//import com.apinews.apiwebnews.Model.News;
//import com.apinews.apiwebnews.Repository.CategoryRepository;
//import com.apinews.apiwebnews.Repository.NewsRepository;
//import com.apinews.apiwebnews.Service.NewsService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.util.List;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class NewsServiceImpl implements NewsService {
//    private final NewsRepository newsRepository;
//    private final CategoryRepository categoryRepository;
//    private static final String UPLOAD_DIR = "C:/Users/tuanv/Pictures/pictureapi/";
//
//
//    @Override
//    public ApiResponse updateNews(Long id, NewsRequest request) {
//        News news = newsRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("News not found"));
//        Category category = categoryRepository.findById(request.getCategoryId())
//                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
//
//        news.setTitle(request.getTitle());
//        news.setContent(request.getContent());
//        news.setCategory(category);
//
//        if (request.getImage() != null && !request.getImage().isEmpty()) {
//            news.setImageUrl(saveImage(request.getImage()));
//        }
//
//        newsRepository.save(news);
//        return new ApiResponse(200, news, "News updated successfully");
//    }
//
//    @Override
//    public ApiResponse createNews(NewsRequest request) {
//        Category category = categoryRepository.findById(request.getCategoryId())
//                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
//
//        News news = new News();
//        news.setTitle(request.getTitle());
//        news.setContent(request.getContent());
//        news.setCategory(category);
//
//        if (request.getImage() != null && !request.getImage().isEmpty()) {
//            String imagePath = saveImage(request.getImage());
//            news.setImageUrl(imagePath);
//        }
//
//        newsRepository.save(news);
//        return new ApiResponse(201, news, "News created successfully");
//    }
//    private String saveImage(MultipartFile image) {
//        try {
//            File uploadFolder = new File(UPLOAD_DIR);
//            if (!uploadFolder.exists()) {
//                uploadFolder.mkdirs();
//            }
//
//            String fileExtension = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."));
//            String fileName = UUID.randomUUID() + fileExtension;
//            Path filePath = Paths.get(UPLOAD_DIR, fileName);
//
//            // Tránh ghi đè file nếu đã tồn tại
//            while (Files.exists(filePath)) {
//                fileName = UUID.randomUUID() + fileExtension;
//                filePath = Paths.get(UPLOAD_DIR, fileName);
//            }
//
//            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//            return "/images/" + fileName;
//        } catch (IOException e) {
//            throw new RuntimeException("Could not save image: " + e.getMessage());
//        }
//    }
//
//
//
//
//
//    private void deleteImage(String imageUrl) {
//        if (imageUrl != null && !imageUrl.isEmpty()) {
//            String filePath = UPLOAD_DIR + imageUrl.replace("/images/", "");
//            File file = new File(filePath);
//            if (file.exists()) {
//                file.delete();
//            }
//        }
//    }
//
//    @Override
//    public ApiResponse deleteNews(Long id) {
//        News news = newsRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("News not found"));
//
//        deleteImage(news.getImageUrl());
//        newsRepository.delete(news);
//        return new ApiResponse(200, true, "News deleted successfully");
//    }
//
//
//    @Override
//    public NewsResponse getNewsById(Long newsId) {
//        News news = newsRepository.findById(newsId)
//                .orElseThrow(() -> new ResourceNotFoundException("News not found"));
//        return toNewsResponse(news);
//    }
//
//    @Override
//    public List<NewsResponse> getAllNews() {
//        return newsRepository.findAll()
//                .stream()
//                .map(this::toNewsResponse)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<NewsResponse> getNewsByCategory(Long categoryId) {
//        Category category = categoryRepository.findById(categoryId)
//                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
//        return newsRepository.findByCategoryId(category.getId())
//                .stream()
//                .map(this::toNewsResponse)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public Page<NewsResponse> getAllNews(int page, int size) {
//        if (page < 0) page = 0;
//        if (size <= 0) size = 10;
//        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
//        return newsRepository.findAll(pageable).map(this::toNewsResponse);
//    }
//
//
//
//    private NewsResponse toNewsResponse(News news) {
//        return NewsResponse.builder()
//                .id(news.getId())
//                .title(news.getTitle())
//                .content(news.getContent())
//                .categoryName(news.getCategory() != null ? news.getCategory().getName() : "Unknown")
//                .imageUrl(news.getImageUrl() != null ? news.getImageUrl() : null)
//                .build();
//    }
//}
package com.apinews.apiwebnews.ServiceImpl;

import com.apinews.apiwebnews.Dto.ApiResponse;
import com.apinews.apiwebnews.Dto.Request.NewsRequest;
import com.apinews.apiwebnews.Dto.Response.NewsResponse;
import com.apinews.apiwebnews.Exception.ResourceNotFoundException;
import com.apinews.apiwebnews.Model.Category;
import com.apinews.apiwebnews.Model.News;
import com.apinews.apiwebnews.Repository.CategoryRepository;
import com.apinews.apiwebnews.Repository.NewsRepository;
import com.apinews.apiwebnews.Service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepository;
    private final CategoryRepository categoryRepository;
    private static final String UPLOAD_DIR = "C:/Users/tuanv/Pictures/pictureapi/";

    @Override
    public ApiResponse updateNews(Long id, NewsRequest request) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("News not found"));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        news.setTitle(request.getTitle());
        news.setContent(request.getContent());
        news.setCategory(category);

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            deleteImage(news.getImageUrl());
            news.setImageUrl(saveImage(request.getImage()));
        }

        newsRepository.save(news);
        return new ApiResponse(200, news, "News updated successfully");
    }

    @Override
    public ApiResponse createNews(NewsRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        News news = new News();
        news.setTitle(request.getTitle());
        news.setContent(request.getContent());
        news.setCategory(category);

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            news.setImageUrl(saveImage(request.getImage()));
        }

        newsRepository.save(news);
        return new ApiResponse(201, news, "News created successfully");
    }

    private String saveImage(MultipartFile image) {
        try {
            File uploadFolder = new File(UPLOAD_DIR);
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs();
            }

            String fileExtension = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."));
            String fileName = UUID.randomUUID() + fileExtension;
            Path filePath = Paths.get(UPLOAD_DIR, fileName);

            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return "/images/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Could not save image: " + e.getMessage());
        }
    }

    private void deleteImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            File file = new File(UPLOAD_DIR + imageUrl.replace("/images/", ""));
            if (file.exists()) {
                file.delete();
            }
        }
    }

    @Override
    public ApiResponse deleteNews(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("News not found"));
        deleteImage(news.getImageUrl());
        newsRepository.delete(news);
        return new ApiResponse(200, true, "News deleted successfully");
    }

    @Override
    public NewsResponse getNewsById(Long newsId) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException("News not found"));
        return toNewsResponse(news);
    }

    @Override
    public List<NewsResponse> getAllNews() {
        return newsRepository.findAll()
                .stream()
                .map(this::toNewsResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<NewsResponse> getNewsByCategory(Long categoryId) {
        return newsRepository.findByCategoryId(categoryId)
                .stream()
                .map(this::toNewsResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<NewsResponse> getAllNews(int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1), Sort.by("id").descending());
        return newsRepository.findAll(pageable).map(this::toNewsResponse);
    }

    private NewsResponse toNewsResponse(News news) {
        return NewsResponse.builder()
                .id(news.getId())
                .title(news.getTitle())
                .content(news.getContent())
                .categoryName(news.getCategory() != null ? news.getCategory().getName() : "Unknown")
                .imageUrl(news.getImageUrl())
                .build();
    }
}