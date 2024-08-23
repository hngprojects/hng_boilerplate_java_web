package hng_java_boilerplate.externalPage.aboutpage.service;

import hng_java_boilerplate.externalPage.aboutpage.dto.AboutPageContentDto;
import hng_java_boilerplate.externalPage.aboutpage.entity.AboutPageContent;
import hng_java_boilerplate.externalPage.aboutpage.repository.AboutPageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AboutPageService {
    private final AboutPageRepository aboutPageRepository;

    @Transactional
    public void updateAboutPageContent(AboutPageContentDto contentDto) {
        AboutPageContent content = new AboutPageContent();
        content.setTitle(contentDto.getTitle());
        content.setIntroduction(contentDto.getIntroduction());

        Map<String, Object> customSections = contentDto.getCustomSections();
        if (customSections != null) {
            Map<String, Object> stats = (Map<String, Object>) customSections.get("stats");
            if (stats != null) {
                content.setYearsInBusiness((Integer) stats.get("years_in_business"));
                content.setCustomers((Integer) stats.get("customers"));
                content.setMonthlyBlogReaders((Integer) stats.get("monthly_blog_readers"));
                content.setSocialFollowers((Integer) stats.get("social_followers"));
            }

            Map<String, Object> services = (Map<String, Object>) customSections.get("services");
            if (services != null) {
                content.setServicesTitle((String) services.get("title"));
                content.setServicesDescription((String) services.get("description"));
            }
        }

        aboutPageRepository.save(content);
    }
}
