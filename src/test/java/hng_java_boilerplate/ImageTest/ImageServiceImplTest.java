package hng_java_boilerplate.ImageTest;

import hng_java_boilerplate.image.entity.Image;
import hng_java_boilerplate.image.repository.ImageRepository;
import hng_java_boilerplate.image.serviceImpl.ImageServiceImpl;
import hng_java_boilerplate.image.util.ImageProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ImageServiceImplTest {

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private ImageServiceImpl imageService;

    private final String storagePath = "/path/image/storage/";
    private final String processedPath = "/path/image/processed/";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUploadImageSuccess() throws IOException {
        String filename = "test.jpg";
        when(file.getOriginalFilename()).thenReturn(filename);
        when(file.getContentType()).thenReturn("image/jpeg");
        when(file.getSize()).thenReturn(100L);
        when(ImageProcessor.compressAndResizeImage(any(MultipartFile.class), anyString())).thenReturn(filename);

        String imageId = imageService.uploadImage(file);

        assertNotNull(imageId);
        verify(imageRepository, times(1)).save(any(Image.class));
    }

    @Test
    void testUploadImageFailure() throws IOException {
        when(file.getOriginalFilename()).thenReturn("test.jpg");
        when(ImageProcessor.compressAndResizeImage(any(MultipartFile.class), anyString())).thenThrow(IOException.class);

        assertThrows(RuntimeException.class, () -> imageService.uploadImage(file));
    }

    @Test
    void testRetrieveImageSuccess() throws IOException {
        String imageId = UUID.randomUUID().toString();
        Image image = new Image();
        image.setId(imageId);
        image.setName("test.jpg");

        when(imageRepository.findById(imageId)).thenReturn(Optional.of(image));
        when(Files.readAllBytes(Paths.get(processedPath + "test.jpg"))).thenReturn(new byte[]{});

        byte[] imageData = imageService.retrieveImage(imageId, null, null);

        assertNotNull(imageData);
    }

    @Test
    void testRetrieveImageNotFound() {
        String imageId = UUID.randomUUID().toString();

        when(imageRepository.findById(imageId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> imageService.retrieveImage(imageId, null, null));
    }
}