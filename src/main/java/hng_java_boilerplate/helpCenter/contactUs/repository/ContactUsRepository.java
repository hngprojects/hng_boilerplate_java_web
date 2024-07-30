package hng_java_boilerplate.helpCenter.contactUs.repository;

import hng_java_boilerplate.helpCenter.contactUs.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactUsRepository extends JpaRepository<Contact, String> {
}
