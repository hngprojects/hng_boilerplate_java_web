package hng_java_boilerplate.profile.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class ProfilePictureResponse {
    private boolean success;
    private String message;
    private String imageUrl;
}
