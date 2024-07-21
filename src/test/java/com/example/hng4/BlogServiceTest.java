package com.example.hng4;

import java.lang.reflect.Method;

import com.example.hng4.DTOs.BlogSearchResponse;
import com.example.hng4.Models.Blog;
import com.example.hng4.Services.BlogService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.typesense.api.Client;
import org.typesense.api.Document;
import org.typesense.model.SearchParameters;
import org.typesense.model.SearchResult;
import org.typesense.model.SearchResultHit;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.typesense.api.Collection;
import org.typesense.api.Documents;

public class BlogServiceTest {

    @Mock
    private Client typesenseClient;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Collection collectionsMock;

     @Mock
    private Documents documentsMock;

    @InjectMocks
    private BlogService blogService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(typesenseClient.collections("blogs")).thenReturn(collectionsMock);
        when(collectionsMock.documents()).thenReturn(documentsMock);
    }

    @Test
    void testSearchBlogs() throws Exception {
        // Setup mock response
        SearchResult searchResult = mock(SearchResult.class);
        SearchResultHit hit = new SearchResultHit();
        Map<String, Object> document = Map.of(
                "id", "1",
                "author", "John Doe",
                "title", "Spring Boot",
                "content", "content",
                "tags", Collections.singletonList("tag"),
                "createdDate", LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().getEpochSecond()
        );
        hit.setDocument(document);
        when(searchResult.getHits()).thenReturn(Collections.singletonList(hit));
        when(searchResult.getFound()).thenReturn(1); 
        when(documentsMock.search(any(SearchParameters.class))).thenReturn(searchResult);
        when(objectMapper.convertValue(document, Blog.class)).thenReturn(new Blog());

        // Perform test
        BlogSearchResponse response = blogService.searchBlogs("John Doe", "Spring Boot", "content", Collections.singletonList("tag"), LocalDate.now(), 1, 10);

        // Validate results
        assertNotNull(response);
        assertEquals(1, response.getTotalResults());
        assertEquals(1, response.getBlogs().size());
    }

    @Test
    void testBuildQuery() throws Exception {
        Long timestamp = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().getEpochSecond();
        
        // Access private method using reflection
        Method method = BlogService.class.getDeclaredMethod("buildQuery", String.class, String.class, String.class, List.class, Long.class);
        method.setAccessible(true);
        
        String query = (String) method.invoke(blogService, "John Doe", "Spring Boot", "content", Collections.singletonList("tag"), timestamp);
        assertEquals("author:=John Doe && title:Spring Boot && content:content && tags:[tag] && createdDate:>=" + timestamp, query);
    }
}
