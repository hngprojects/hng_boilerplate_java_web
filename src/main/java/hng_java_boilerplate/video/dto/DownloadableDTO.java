package hng_java_boilerplate.video.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.core.io.ByteArrayResource;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class DownloadableDTO {
    private String contentType;
    private int videoByteLength;
    private ByteArrayResource resource;
}
