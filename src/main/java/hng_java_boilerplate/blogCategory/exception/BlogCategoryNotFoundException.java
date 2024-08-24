package hng_java_boilerplate.blogCategory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class BlogCategoryNotFoundException extends RuntimeException {

    public BlogCategoryNotFoundException(String message) {
        super(message);
    }
}
