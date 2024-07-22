package hng_java_boilerplate.about_page_test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import hng_java_boilerplate.about_page.entity.AboutPage;
import hng_java_boilerplate.about_page.entity.AboutPageDto;
import hng_java_boilerplate.about_page.entity.AboutPageMapperClass;
import hng_java_boilerplate.about_page.repository.AboutPageRepository;
import hng_java_boilerplate.about_page.service.impl.AboutPageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

class AboutPageServiceImplTest {

    @Mock
    private AboutPageRepository aboutPageRepository;

    @Mock
    private AboutPageMapperClass aboutPageMapper;

    @InjectMocks
    private AboutPageServiceImpl aboutPageService;

    private AboutPage aboutPage;
    private AboutPageDto aboutPageDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        aboutPage = new AboutPage();
        aboutPage.setId(1L);
        aboutPage.setTitle("Old Title");
        aboutPage.setIntroduction("Old Introduction");

        aboutPageDto = new AboutPageDto();
        aboutPageDto.setId(1L);
        aboutPageDto.setTitle("New Title");
        aboutPageDto.setIntroduction("New Introduction");

        when(aboutPageRepository.findById(1L)).thenReturn(Optional.of(aboutPage));
        when(aboutPageRepository.save(any(AboutPage.class))).thenReturn(aboutPage);
        when(aboutPageMapper.mapAboutPageToAboutPageDto(any(AboutPage.class))).thenReturn(aboutPageDto);
    }

    @Test
    void testUpdateAboutPage_Success() {
        AboutPageDto updatedDto = aboutPageService.updateAboutPage(1L, aboutPageDto);

        assertEquals("New Title", updatedDto.getTitle());
        assertEquals("New Introduction", updatedDto.getIntroduction());
    }

    @Test
    void testUpdateAboutPage_WrongInputs() {
        aboutPageDto.setTitle(null); // Example of wrong input

        assertThrows(RuntimeException.class, () -> {
            aboutPageService.updateAboutPage(1L, aboutPageDto);
        });
    }


    @Test
    void testUpdateAboutPage_WrongId() {
        when(aboutPageRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            aboutPageService.updateAboutPage(2L, aboutPageDto);
        });
    }
}

