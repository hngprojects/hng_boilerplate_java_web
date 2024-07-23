package hng_java_boilerplate.blog.controller;

import hng_java_boilerplate.blog.dto.EditRequest;
import hng_java_boilerplate.blog.dto.EditResponse;
import hng_java_boilerplate.blog.service.EditBlogService;
import hng_java_boilerplate.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/blog")
public class BlogController {
    private final EditBlogService editBlogService;


    @PutMapping("/edit/{blog_id}")
    public ResponseEntity<ApiResponse<EditResponse>> edit_blog(@PathVariable String blog_id,
                                                               @RequestBody @Validated EditRequest request){
        return editBlogService.editBlog(blog_id, request);
    }
}
