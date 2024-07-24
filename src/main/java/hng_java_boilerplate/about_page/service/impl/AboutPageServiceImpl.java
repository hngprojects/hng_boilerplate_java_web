package hng_java_boilerplate.about_page.service.impl;


import hng_java_boilerplate.about_page.entity.AboutPage;
import hng_java_boilerplate.about_page.entity.AboutPageDto;
import hng_java_boilerplate.about_page.entity.AboutPageMapperClass;
import hng_java_boilerplate.about_page.repository.AboutPageRepository;
import hng_java_boilerplate.about_page.service.AboutPageService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AboutPageServiceImpl implements AboutPageService {
    private final AboutPageRepository aboutPageRepository;
    private final AboutPageMapperClass aboutPageMapper;

    @Autowired
    public AboutPageServiceImpl(AboutPageRepository aboutPageRepository, AboutPageMapperClass aboutPageMapper) {
        this.aboutPageRepository = aboutPageRepository;
        this.aboutPageMapper = aboutPageMapper;
    }

    @Override
    @Transactional
    public AboutPageDto updateAboutPage(Long id, AboutPageDto aboutPageDto) {
        if (aboutPageDto.getTitle() == null || aboutPageDto.getTitle().isEmpty()) {
            throw new RuntimeException("Title cannot be empty");
        }
        if (aboutPageDto.getIntroduction() == null || aboutPageDto.getIntroduction().isEmpty()) {
            throw new RuntimeException("Introduction cannot be empty");
        }
        if (aboutPageDto.getCustomSections() != null) {
            if (aboutPageDto.getCustomSections().getStats() != null) {
                AboutPageDto.Stats stats = aboutPageDto.getCustomSections().getStats();
                if (stats.getYearsInBusiness() < 0) {
                    throw new RuntimeException("Years in business cannot be negative");
                }
                if (stats.getCustomers() < 0) {
                    throw new RuntimeException("Customers cannot be negative");
                }
                if (stats.getMonthlyBlogReaders() < 0) {
                    throw new RuntimeException("Monthly blog readers cannot be negative");
                }
                if (stats.getSocialFollowers() < 0) {
                    throw new RuntimeException("Social followers cannot be negative");
                }
            }
            if (aboutPageDto.getCustomSections().getServices() != null) {
                AboutPageDto.Services services = aboutPageDto.getCustomSections().getServices();
                if (services.getTitle() == null || services.getTitle().isEmpty()) {
                    throw new RuntimeException("Service title cannot be empty");
                }
                if (services.getDescription() == null || services.getDescription().isEmpty()) {
                    throw new RuntimeException("Service description cannot be empty");
                }
            }
        }

        AboutPage aboutPage = aboutPageRepository.findById(id).orElseThrow(() -> new RuntimeException("About page not found!"));
        aboutPageMapper.mapAboutPageDtoToAboutPage(aboutPageDto);
        aboutPage = aboutPageRepository.save(aboutPage);
        return aboutPageMapper.mapAboutPageToAboutPageDto(aboutPage);
    }

    @Override
    public AboutPageDto createAboutPage(AboutPageDto aboutPageDto) {
        AboutPage aboutPage = AboutPageMapperClass.mapAboutPageDtoToAboutPage(aboutPageDto);
        AboutPage savedAboutPage = aboutPageRepository.save(aboutPage);
        return AboutPageMapperClass.mapAboutPageToAboutPageDto(savedAboutPage);
    }

    @Override
    public AboutPageDto getAboutPage(Long id) {
        AboutPage aboutPage = aboutPageRepository.findById(id).orElseThrow(() -> new RuntimeException("About page not found!"));
        return AboutPageMapperClass.mapAboutPageToAboutPageDto(aboutPage);
    }
}

