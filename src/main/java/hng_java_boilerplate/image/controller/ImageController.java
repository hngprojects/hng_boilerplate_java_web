package hng_java_boilerplate.image.controller;


import hng_java_boilerplate.image.service.ImageService;
import hng_java_boilerplate.image.util.ContentTypeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile image) {
        try {
            String imageId = imageService.uploadImage(image);
            return new ResponseEntity<>(Map.of(
                    "message", "Image uploaded successfully.",
                    "imageId", imageId,
                    "statusCode", 201
            ), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of(
                    "message", "Failed to upload image.",
                    "statusCode", 500
            ), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> retrieveImage(
            @PathVariable String id,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String format) {
        try {
            byte[] imageData = imageService.retrieveImage(id, size, format);
            MediaType contentType = ContentTypeUtil.getMediaType(format);
            return ResponseEntity.ok()
                    .contentType(contentType)
                    .body(imageData);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of(
                    "message", "Failed to retrieve image.",
                    "statusCode", 500
            ), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
