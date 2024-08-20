package hng_java_boilerplate.audio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.mail.MailParseException;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AudioResponseDTO {
    private String jobId;
    private String status;
    private Map<String, String> data;
}

