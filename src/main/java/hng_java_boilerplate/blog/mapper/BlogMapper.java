package hng_java_boilerplate.blog.mapper;

import hng_java_boilerplate.blog.dto.EditRequest;
import hng_java_boilerplate.blog.dto.EditResponse;
import hng_java_boilerplate.blog.entity.Blog;

import java.time.format.DateTimeFormatter;

public class BlogMapper {
    public static Blog mapDtoToBlog(EditRequest request, Blog blog){
        blog.setTitle(request.getTitle());
        blog.setContent(request.getContent());
        blog.setTags(request.getTags());
        return blog;
    }

    public static EditResponse mapBlogToDto(Blog blog, EditResponse response){
        response.setId(blog.getBlogId());
        response.setTitle(blog.getTitle());
        response.setContent(blog.getContent());
        response.setTags(blog.getTags());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String updated_time = blog.getUpdated_at().format(formatter);
        response.setUpdated_at(updated_time);
        return response;
    }
}
