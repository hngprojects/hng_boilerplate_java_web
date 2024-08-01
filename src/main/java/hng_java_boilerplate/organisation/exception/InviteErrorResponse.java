package hng_java_boilerplate.organisation.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InviteErrorResponse {

        private String message;
        private List<String> error;
        private int status;

}
