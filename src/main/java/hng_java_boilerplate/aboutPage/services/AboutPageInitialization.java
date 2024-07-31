package hng_java_boilerplate.aboutPage.services;

import hng_java_boilerplate.aboutPage.entities.AboutPage;
import hng_java_boilerplate.aboutPage.entities.CustomSection;
import hng_java_boilerplate.aboutPage.entities.ServiceEntity;
import hng_java_boilerplate.aboutPage.entities.StatEntity;
import hng_java_boilerplate.aboutPage.repository.AboutPageRepository;
import hng_java_boilerplate.aboutPage.repository.CustomSectionRepository;
import hng_java_boilerplate.aboutPage.repository.ServiceRepository;
import hng_java_boilerplate.aboutPage.repository.StatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AboutPageInitialization {
    private final AboutPageRepository aboutPageRepository;
    private final ServiceRepository serviceRepository;
    private final CustomSectionRepository customSectionRepository;
    private final StatRepository statRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeDefaultAboutPage(){
        if (aboutPageRepository.count()==0){
            AboutPage aboutPage = new AboutPage();
            ServiceEntity service = new ServiceEntity();
            CustomSection customSection = new CustomSection();
            StatEntity statEntity = new StatEntity();

            aboutPage.setTitle("HNG boilerplate code");
            aboutPage.setIntroduction("Trained to give you the best boiler codes");
            aboutPageRepository.save(aboutPage);

            customSection.setAboutPage(aboutPage);
            customSectionRepository.save(customSection);

            statEntity.setYears_in_business(10);
            statEntity.setCustomers(75000);
            statEntity.setMonthly_blog_readers(100000);
            statEntity.setSocial_followers(1200000);
            statEntity.setCustom_sections(customSection);
            statRepository.save(statEntity);

            service.setTitle("Trained to give you the best");
            service.setDescription("HNG is dedicated to give you the very best in learning");
            service.setCustom_sections(customSection);
            serviceRepository.save(service);
        }
    }
}
