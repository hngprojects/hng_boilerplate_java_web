package hng_java_boilerplate.about_page.service;


import hng_java_boilerplate.about_page.entity.AboutPageDto;
import org.springframework.stereotype.Service;

@Service
public interface AboutPageService {

    AboutPageDto updateAboutPage(Long id, AboutPageDto aboutPageDto);

}
