package hng_java_boilerplate.blogPost.service;

import hng_java_boilerplate.blogPost.controller.dto.BlogPostDTO;
import hng_java_boilerplate.blogPost.entity.BlogPost;
import org.springframework.stereotype.Service;

@Service
public interface BlogPostService {


    BlogPostDTO save(BlogPostDTO post);

    void delete(BlogPost blogId);


}