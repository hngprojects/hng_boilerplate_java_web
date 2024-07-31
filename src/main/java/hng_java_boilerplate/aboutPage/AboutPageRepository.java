package hng_java_boilerplate.aboutPage;


import hng_java_boilerplate.aboutPage.entities.AboutPage;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AboutPageRepository extends JpaRepository<AboutPage, Long> {

}
