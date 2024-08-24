package hng_java_boilerplate.blogPost.service;

import hng_java_boilerplate.blogPost.controller.dto.BlogPostDTO;
import hng_java_boilerplate.blogPost.entity.BlogPost;
import hng_java_boilerplate.blogPost.repository.BlogPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlogPostServiceImpl implements BlogPostService {
    private final BlogPostRepository blogPostRepository;

    @Override
    public BlogPostDTO save(BlogPostDTO post) {

        BlogPost savedPost = new BlogPost();
        BlogPostDTO saveDTO = new BlogPostDTO();
        saveDTO.setTitle(savedPost.getTitle());
        saveDTO.setContent(savedPost.getContent());
        saveDTO.setImageUrls(savedPost.getImageUrls());
        saveDTO.setTags(savedPost.getTags());

        return saveDTO;
    }
}
