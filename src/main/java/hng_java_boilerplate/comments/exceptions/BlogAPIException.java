package hng_java_boilerplate.comments.exceptions;

    import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

    @Getter
    @Setter
    public class BlogAPIException  extends RuntimeException{

        private HttpStatus httpStatus;
        private String message;

        public BlogAPIException(HttpStatus httpStatus, String message) {
            this.httpStatus = httpStatus;
            this.message = message;
        }

        public BlogAPIException(String message, HttpStatus httpStatus,String message1) {
            super(message);
            this.message=message1;
            this.httpStatus=httpStatus;
        }
    }

