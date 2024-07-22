package hng_java_boilerplate.blog.service;

import hng_java_boilerplate.blog.dto.EditRequest;
import hng_java_boilerplate.blog.dto.EditResponse;
import hng_java_boilerplate.blog.entity.Blog;
import hng_java_boilerplate.blog.mapper.BlogMapper;
import hng_java_boilerplate.blog.repository.BlogRepository;
import hng_java_boilerplate.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class EditBlogImplementation implements EditBlogService {
    private final BlogRepository blogRepository;
    private final Pattern TAG_PATTERN = Pattern.compile("^[a-zA-Z0-9-_ ]+$");
    private final int MAXIMUM_TAG_LENGTH = 30;

    /**
     * Allows super admin to edit a blog post
     * @param blog_id
     * @param request
     * @returns api response containing an EditResponse object
     */
    @Override
    public ResponseEntity<ApiResponse<EditResponse>> editBlog(String blog_id, EditRequest request) {
        //some checks to be done here later
        Set<String> unique_tags = new HashSet<>();
        List<String> formatted_tags = new ArrayList<>();
        for(String tag : request.getTags()){
            String formatted_tag = formatTag(tag);
            if(isValidTag(formatted_tag) && unique_tags.add(formatted_tag)){
                formatted_tags.add(formatted_tag);
            }
        }
        request.setTags(formatted_tags);

        Optional<Blog> blogOptional = blogRepository.findByBlogId(blog_id);
        if(!blogOptional.isPresent()){
            return new ResponseEntity<>(new ApiResponse<>("Bad Request",
                    "Blog post does not exist"), HttpStatus.BAD_REQUEST);
        } else{
            Blog saved_blog = blogRepository.save(BlogMapper.mapDtoToBlog(request, blogOptional.get()));
            return new ResponseEntity<>(new ApiResponse<>("Blog post successfully updated",
                    BlogMapper.mapBlogToDto(saved_blog, new EditResponse())), HttpStatus.OK);
        }
    }

    private boolean isValidTag(String tag){
        if(tag == null || tag.length() > MAXIMUM_TAG_LENGTH){
            return false;
        }
        return TAG_PATTERN.matcher(tag).matches();
    }
    private String formatTag(String tag){
        if(tag == null){
            return tag;
        }
        return tag.trim();
    }

}
