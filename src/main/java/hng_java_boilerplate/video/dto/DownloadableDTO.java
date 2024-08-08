package hng_java_boilerplate.video.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.ByteArrayResource;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DownloadableDTO {
    int videoByteLength;
    ByteArrayResource resource;
}
