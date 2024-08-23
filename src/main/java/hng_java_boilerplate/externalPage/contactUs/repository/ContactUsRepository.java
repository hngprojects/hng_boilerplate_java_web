package hng_java_boilerplate.externalPage.contactUs.repository;

import hng_java_boilerplate.externalPage.contactUs.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactUsRepository extends JpaRepository<Contact, String> {
}
