package hng_java_boilerplate.image.serviceImpl;

import hng_java_boilerplate.image.entity.Image;
import hng_java_boilerplate.image.repository.ImageRepository;
import hng_java_boilerplate.image.service.ImageService;
import hng_java_boilerplate.image.util.ImageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }



    private final String storagePath = "/path/image/storage/";
    private final String processedPath = "/path/image/processed/";

    @Override
    public String uploadImage(MultipartFile file) {
        try {
            String filename = ImageProcessor.compressAndResizeImage(file, storagePath);
            ImageProcessor.generateDifferentSizes(storagePath, processedPath, filename);

            Image image = new Image();
            image.setId(UUID.randomUUID().toString());
            image.setName(filename);
            image.setType(file.getContentType());
            image.setSize(file.getSize());
            image.setPath(storagePath + filename);

            imageRepository.save(image);
            return image.getId();
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    @Override
    public byte[] retrieveImage(String id, String size, String format) {
        Optional<Image> imageOpt = imageRepository.findById(id);
        if (imageOpt.isPresent()) {
            Image image = imageOpt.get();
            String imagePath = processedPath + (size != null ? size + "-" : "") + image.getName();

            try {
                return Files.readAllBytes(Paths.get(imagePath));
            } catch (IOException e) {
                throw new RuntimeException("Failed to retrieve image", e);
            }
        } else {
            throw new RuntimeException("Image not found");
        }
    }
    //check
}
