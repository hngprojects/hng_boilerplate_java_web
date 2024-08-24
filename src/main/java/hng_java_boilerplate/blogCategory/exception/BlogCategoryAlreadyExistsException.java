package hng_java_boilerplate.blogCategory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BlogCategoryAlreadyExistsException extends RuntimeException {
    public BlogCategoryAlreadyExistsException(String message) {
        super(message);
    }
}
