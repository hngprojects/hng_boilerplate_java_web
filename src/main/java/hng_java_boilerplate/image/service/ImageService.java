package hng_java_boilerplate.image.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String uploadImage(MultipartFile file);
    byte[] retrieveImage(String id, String size, String format);
}
