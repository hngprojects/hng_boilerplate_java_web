package hng_java_boilerplate.blog.service;

import hng_java_boilerplate.blog.dto.EditRequest;
import hng_java_boilerplate.blog.dto.EditResponse;
import hng_java_boilerplate.payload.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface EditBlogService {
    ResponseEntity<ApiResponse<EditResponse>> editBlog(String blog_id, EditRequest request);
}
