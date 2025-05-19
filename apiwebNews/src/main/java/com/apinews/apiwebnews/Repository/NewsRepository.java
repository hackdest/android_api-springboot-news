package com.apinews.apiwebnews.Repository;



import com.apinews.apiwebnews.Model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findByCategoryId(Long categoryId);
}
