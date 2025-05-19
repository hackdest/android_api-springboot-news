package com.apinews.apiwebnews.Service;


import com.apinews.apiwebnews.Dto.Request.CategoryRequest;
import com.apinews.apiwebnews.Dto.Response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest request);
    CategoryResponse updateCategory(Long categoryId, CategoryRequest request);
    CategoryResponse getCategoryById(Long categoryId);
    void deleteCategory(Long categoryId);
    List<CategoryResponse> getAllCategories();
}
