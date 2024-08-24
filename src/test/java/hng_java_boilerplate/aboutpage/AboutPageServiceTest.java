package hng_java_boilerplate.aboutpage;

import hng_java_boilerplate.aboutpage.dto.AboutPageContentDto;
import hng_java_boilerplate.aboutpage.entity.AboutPageContent;
import hng_java_boilerplate.aboutpage.repository.AboutPageRepository;
import hng_java_boilerplate.aboutpage.service.AboutPageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class AboutPageServiceTest {

    @Mock
    private AboutPageRepository aboutPageRepository;

    @InjectMocks
    private AboutPageService aboutPageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdateAboutPageContent() {
        // Prepare the input DTO
        AboutPageContentDto contentDto = new AboutPageContentDto();
        contentDto.setTitle("More Than Just A BoilerPlate");
        contentDto.setIntroduction("Welcome to Hng Boilerplate, where passion meets innovation.");

        Map<String, Object> customSections = new HashMap<>();
        Map<String, Object> stats = new HashMap<>();
        stats.put("years_in_business", 10);
        stats.put("customers", 75000);
        stats.put("monthly_blog_readers", 100000);
        stats.put("social_followers", 1200000);
        customSections.put("stats", stats);

        Map<String, Object> services = new HashMap<>();
        services.put("title", "Trained to Give You The Best");
        services.put("description", "Since our founding, Hng Boilerplate has been dedicated to constantly evolving to stay ahead of the curve.");
        customSections.put("services", services);

        contentDto.setCustomSections(customSections);

        // Call the method to test
        aboutPageService.updateAboutPageContent(contentDto);

        // Verify interactions with the repository
        ArgumentCaptor<AboutPageContent> captor = ArgumentCaptor.forClass(AboutPageContent.class);
        verify(aboutPageRepository).save(captor.capture());

        AboutPageContent savedContent = captor.getValue();
        assertEquals("More Than Just A BoilerPlate", savedContent.getTitle());
        assertEquals("Welcome to Hng Boilerplate, where passion meets innovation.", savedContent.getIntroduction());
        assertEquals(10, savedContent.getYearsInBusiness());
        assertEquals(75000, savedContent.getCustomers());
        assertEquals(100000, savedContent.getMonthlyBlogReaders());
        assertEquals(1200000, savedContent.getSocialFollowers());
        assertEquals("Trained to Give You The Best", savedContent.getServicesTitle());
        assertEquals("Since our founding, Hng Boilerplate has been dedicated to constantly evolving to stay ahead of the curve.", savedContent.getServicesDescription());
    }

    @Test
    void deleteAboutPageContent_ShouldDeleteContent() {
        assertDoesNotThrow(() -> aboutPageService.deleteAboutPageContent());
        verify(aboutPageRepository, times(1)).deleteAll();
    }
}
