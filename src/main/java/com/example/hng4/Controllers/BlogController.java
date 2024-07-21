package com.example.hng4.Controllers;




import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.typesense.api.exceptions.ObjectNotFound;

import com.example.hng4.DTOs.BlogSearchResponse;
import com.example.hng4.Models.Blog;
import com.example.hng4.Models.ErrorResponse;
import com.example.hng4.Services.BlogService;

@RestController
@RequestMapping("/api/v1/blogs")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/search")
   public ResponseEntity<?> searchBlogs(
        @RequestParam(required = false) String author,
        @RequestParam(required = false) String title,
        @RequestParam(required = false) String content,
        @RequestParam(required = false) List<String> tags,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdDate,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int pageSize) {
            try {
                BlogSearchResponse response = blogService.searchBlogs(author, title, content, tags, createdDate, page, pageSize);
                if (response.getBlogs().isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("No blogs found matching the criteria", "Not Found", HttpStatus.NOT_FOUND.value()));
                }
                return ResponseEntity.ok(response);
            } catch (ObjectNotFound e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("The requested resource was not found", e.getMessage(), HttpStatus.NOT_FOUND.value()));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("Invalid search parameters", e.getMessage(), HttpStatus.BAD_REQUEST.value()));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("An error occurred while searching blogs", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
            }
        }
}

