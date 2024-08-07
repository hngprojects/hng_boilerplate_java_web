package hng_java_boilerplate.image.util;

import org.springframework.http.MediaType;

public class ContentTypeUtil {
    public static MediaType getMediaType(String format) {
        if (format == null) {
            return MediaType.IMAGE_JPEG;
        }
        switch (format.toLowerCase()) {
            case "png":
                return MediaType.IMAGE_PNG;
            case "webp":
                return MediaType.parseMediaType("image/webp");
            case "jpeg":
            default:
                return MediaType.IMAGE_JPEG;
        }
    }
}
