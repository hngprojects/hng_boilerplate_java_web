package com.example.hng4.Repositories;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.hng4.Models.Blog;

public interface BlogRepository extends JpaRepository<Blog, Long> {

        @Query("SELECT b FROM Blog b WHERE " +
        "(:author IS NULL OR b.author = :author) AND " +
        "(:title IS NULL OR b.title LIKE %:title%) AND " +
        "(:content IS NULL OR b.content LIKE %:content%) AND " +
        "(:tags IS NULL OR EXISTS (SELECT 1 FROM b.tags t WHERE t IN :tags)) AND " +
        "(:createdDate IS NULL OR b.createdDate >= :createdDate)")
        List<Blog> searchBlogs(@Param("author") String author,
                       @Param("title") String title,
                       @Param("content") String content,
                       @Param("tags") List<String> tags,
                       @Param("createdDate") LocalDate createdDate);
}
