package hng_java_boilerplate.video.dto;

import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FinishedDTO {
    private String jobId;
    private byte[] media;
}
