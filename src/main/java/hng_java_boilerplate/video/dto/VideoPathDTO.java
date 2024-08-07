package hng_java_boilerplate.video.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class VideoPathDTO {

    private String jobId;
    private Map<String, byte[]> video;

    public void addVideo(String key, byte[] value){
        if (video == null) {
            video = new HashMap<>();
        }
        video.put(key, value);
    }
}
