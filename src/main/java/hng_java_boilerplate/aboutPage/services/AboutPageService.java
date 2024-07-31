package hng_java_boilerplate.aboutPage.services;
import hng_java_boilerplate.aboutPage.dtos.AboutPageDTO;
import hng_java_boilerplate.aboutPage.dtos.RetrievalDTO;
import hng_java_boilerplate.aboutPage.entities.AboutPage;
import hng_java_boilerplate.aboutPage.repository.AboutPageRepository;
import hng_java_boilerplate.aboutPage.repository.CustomSectionRepository;
import hng_java_boilerplate.aboutPage.repository.ServiceRepository;
import hng_java_boilerplate.aboutPage.repository.StatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class AboutPageService {
    private final AboutPageRepository aboutPageRepository;
    private final ServiceRepository serviceRepository;
    private final CustomSectionRepository customSectionRepository;
    private final StatRepository statRepository;

    @Transactional
    public RetrievalDTO getAboutPage(){
        AboutPage aboutPage = aboutPageRepository.findAll().stream().findFirst().orElseThrow(() -> new RuntimeException("About page not found"));
        RetrievalDTO aboutPageDto = new RetrievalDTO();
        aboutPageDto.setTitle(aboutPage.getTitle());
        aboutPageDto.setIntroduction(aboutPage.getIntroduction());

        aboutPageDto.getCustom_sections().getStats().setCustomers(aboutPage.getCustom_sections().getStats().getCustomers());
        aboutPageDto.getCustom_sections().getStats().setYears_in_business(aboutPage.getCustom_sections().getStats().getYears_in_business());
        aboutPageDto.getCustom_sections().getStats().setSocial_followers(aboutPage.getCustom_sections().getStats().getSocial_followers());
        aboutPageDto.getCustom_sections().getStats().setMonthly_blog_readers(aboutPage.getCustom_sections().getStats().getMonthly_blog_readers());

        aboutPageDto.getCustom_sections().getServices().setTitle(aboutPage.getCustom_sections().getServices().getTitle());
        aboutPageDto.getCustom_sections().getServices().setDescription(aboutPage.getCustom_sections().getServices().getDescription());

        aboutPageDto.setStatus_code(200);
        aboutPageDto.setMessage("Retrieved about page content successfully");
        return aboutPageDto;
    }
    @Transactional
    public AboutPageDTO updateAboutPage(AboutPageDTO aboutPageDTO){
        AboutPage aboutPage = aboutPageRepository.findAll().stream().findFirst().orElseThrow(() -> new RuntimeException("About page not found"));
        aboutPage.setTitle(aboutPageDTO.getTitle());
        aboutPage.setIntroduction(aboutPageDTO.getIntroduction());

        aboutPage.getCustom_sections().getStats().setCustomers(aboutPageDTO.getCustom_sections().getStats().getCustomers());
        aboutPage.getCustom_sections().getStats().setYears_in_business(aboutPageDTO.getCustom_sections().getStats().getYears_in_business());
        aboutPage.getCustom_sections().getStats().setSocial_followers(aboutPageDTO.getCustom_sections().getStats().getSocial_followers());
        aboutPage.getCustom_sections().getStats().setMonthly_blog_readers(aboutPageDTO.getCustom_sections().getStats().getMonthly_blog_readers());

        aboutPage.getCustom_sections().getServices().setTitle(aboutPageDTO.getCustom_sections().getServices().getTitle());
        aboutPage.getCustom_sections().getServices().setDescription(aboutPageDTO.getCustom_sections().getServices().getDescription());
        aboutPageRepository.save(aboutPage);
        return aboutPageDTO;
    }
}

