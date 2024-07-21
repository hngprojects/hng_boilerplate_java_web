package com.example.hng4;


import com.example.hng4.Models.Blog;
import com.example.hng4.Models.ErrorResponse;
import com.example.hng4.Services.BlogService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.typesense.api.exceptions.ObjectNotFound;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.example.hng4.Controllers.BlogController;
import com.example.hng4.DTOs.BlogSearchResponse;

public class BlogControllerTest {

    @Mock
    private BlogService blogService;

    @InjectMocks
    private BlogController blogController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @SuppressWarnings("deprecation")
    @Test
    void testSearchBlogs_NotFound() throws Exception {
        BlogSearchResponse response = new BlogSearchResponse();
        response.setCurrentPage(1);
        response.setTotalPages(1);
        response.setTotalResults(0);
        response.setBlogs(Collections.emptyList());
        response.setMeta(null);

        when(blogService.searchBlogs(anyString(), anyString(), anyString(), anyList(), any(LocalDate.class), anyInt(), anyInt()))
                .thenReturn(response);

        ResponseEntity<?> responseEntity = blogController.searchBlogs("John Doe", "Spring Boot", "content", Collections.singletonList("tag"), LocalDate.now(), 1, 10);

        assertEquals(404, responseEntity.getStatusCodeValue());
        assertEquals(ErrorResponse.class, responseEntity.getBody().getClass());
    }

  
    @Test
    void testSearchBlogs_Success() throws Exception {
        // Setup mock response
        Blog blog = new Blog();
        blog.setId(1L);
        blog.setAuthor("John Doe");
        blog.setTitle("Spring Boot");
        blog.setContent("content");
        blog.setTags(Collections.singletonList("tag"));
        blog.setCreatedDate(LocalDate.now());

        BlogSearchResponse response = new BlogSearchResponse();
        response.setBlogs(Collections.singletonList(blog));
        response.setTotalResults(1);
        response.setTotalPages(1);
        response.setCurrentPage(1);
        BlogSearchResponse.Meta meta = new BlogSearchResponse.Meta();
        meta.setHasNext(false);
        meta.setTotal(1);
        meta.setNextPage(null);
        meta.setPrevPage(null);
        response.setMeta(meta);

        when(blogService.searchBlogs(anyString(), anyString(), anyString(), anyList(), any(LocalDate.class), anyInt(), anyInt())).thenReturn(response);

        // Perform test
        ResponseEntity<?> result = blogController.searchBlogs("John Doe", "Spring Boot", "content", Collections.singletonList("tag"), LocalDate.now(), 1, 10);

        // Validate results
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        BlogSearchResponse responseBody = (BlogSearchResponse) result.getBody();
        assertNotNull(responseBody);
        assertEquals(1, responseBody.getTotalResults());
        assertEquals(1, responseBody.getBlogs().size());
    }


@Test
void testSearchBlogs_ObjectNotFound() throws Exception {
    when(blogService.searchBlogs(anyString(), anyString(), anyString(), anyList(), any(LocalDate.class), anyInt(), anyInt()))
            .thenThrow(ObjectNotFound.class);

    ResponseEntity<?> response = blogController.searchBlogs("John Doe", "Spring Boot", "content", Collections.singletonList("tag"), LocalDate.now(), 1, 10);

    assertEquals(404, response.getStatusCodeValue());
    assertEquals(ErrorResponse.class, response.getBody().getClass());
}
}
