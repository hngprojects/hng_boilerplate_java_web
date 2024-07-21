package com.example.hng4.Services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.typesense.api.Client;
import org.typesense.model.SearchParameters;
import org.typesense.model.SearchResult;
import org.typesense.model.SearchResultHit;

import com.example.hng4.DTOs.BlogSearchResponse;
import com.example.hng4.Models.Blog;
import com.example.hng4.Repositories.BlogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private Client typesenseClient;

    @Autowired
    private ObjectMapper objectMapper;

    public BlogSearchResponse searchBlogs(String author, String title, String content, List<String> tags, LocalDate createdDate, int page, int pageSize) throws Exception {
        try {

             // Log search parameters
             System.out.println("Search parameters: author=" + author + ", title=" + title + ", content=" + content + ", tags=" + tags + ", createdDate=" + createdDate + ", page=" + page + ", pageSize=" + pageSize);
             // Convert createdDate to Unix timestamp if it's not null
             Long unixTimestamp = null;
             if (createdDate != null) {
                 unixTimestamp = createdDate.atStartOfDay(ZoneId.systemDefault()).toInstant().getEpochSecond();
                 System.out.println("Converted createdDate to Unix timestamp: " + unixTimestamp);
             }
             String query = buildQuery(author, title, content, tags, unixTimestamp);

             // Log built query
             System.out.println("Built query: " + query);
            
            // Create a SearchParameters object
            SearchParameters searchParameters = new SearchParameters();
            searchParameters.setQ("*");
            searchParameters.setQueryBy("author,title,content,tags");
            searchParameters.setFilterBy(query);
            searchParameters.setPage(page);
            searchParameters.setPerPage(pageSize);
            

              // Log search parameters
              System.out.println("Search parameters: " + searchParameters);

            // Search using the parameters
            SearchResult searchResult = typesenseClient.collections("blogs").documents().search(searchParameters);
            List<SearchResultHit> hits = searchResult.getHits();
            List<Blog> blogs = new ArrayList<>();
            for (SearchResultHit hit : hits) {
                Map<String, Object> document = (Map<String, Object>) hit.getDocument();
                Blog blog = objectMapper.convertValue(document, Blog.class);
                blogs.add(blog);
            }
               // Calculate total pages based on the number of results and page size
               int totalResults = searchResult.getFound();
               int totalPages = (int) Math.ceil((double) totalResults / pageSize);

            // Build response
            BlogSearchResponse response = new BlogSearchResponse();
            response.setCurrentPage(searchResult.getPage());
            response.setTotalPages(totalPages);
            response.setTotalResults(totalResults);
            response.setBlogs(blogs);

            BlogSearchResponse.Meta meta = new BlogSearchResponse.Meta();
            meta.setHasNext(page < totalPages);
            meta.setTotal(totalResults);
            meta.setNextPage(page < totalPages ? page + 1 : null);
            meta.setPrevPage(page > 1 ? page - 1 : null);
            response.setMeta(meta);

            return response;
        } 
        catch (org.typesense.api.exceptions.ObjectNotFound e) {
            System.err.println("Collection 'blogs' not found in Typesense: " + e.getMessage());
            throw new RuntimeException("Typesense collection not found", e);
        } catch (Exception e) {
            System.err.println("Error searching Typesense: " + e.getMessage());
            e.printStackTrace(); // This will print the full stack trace
            throw new RuntimeException("Error searching blogs", e);
        }
    }

    private String buildQuery(String author, String title, String content, List<String> tags, Long createdDate) {
        StringBuilder filter = new StringBuilder();
        if (author != null && !author.isEmpty()) filter.append("author:=").append(author).append(" && ");
        if (title != null && !title.isEmpty()) filter.append("title:").append(title).append(" && ");
        if (content != null && !content.isEmpty()) filter.append("content:").append(content).append(" && ");
        if (tags != null && !tags.isEmpty()) filter.append("tags:[").append(String.join(",", tags)).append("] && ");
        if (createdDate != null) filter.append("createdDate:>=").append(createdDate).append(" && ");
        if (filter.length() > 0) filter.setLength(filter.length() - 4); // Remove the last " && "
        System.out.println("Built filter query: " + filter.toString());
        return filter.toString();
    }
    private String buildSearchQuery(String author, String title, String content) {
        List<String> queryParts = new ArrayList<>();
        if (author != null && !author.isEmpty()) queryParts.add(author);
        if (title != null && !title.isEmpty()) queryParts.add(title);
        if (content != null && !content.isEmpty()) queryParts.add(content);
        return queryParts.isEmpty() ? "*" : String.join(" ", queryParts);
    }
}
