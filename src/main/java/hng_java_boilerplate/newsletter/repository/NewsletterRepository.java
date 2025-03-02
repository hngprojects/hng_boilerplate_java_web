package hng_java_boilerplate.newsletter.repository;


import hng_java_boilerplate.newsletter.entity.Newsletter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface NewsletterRepository extends JpaRepository<Newsletter, String> {

    Page<Newsletter> findByUser_Id(String userId, Pageable page);

    @Query("SELECT n FROM Newsletter n WHERE n.createdAt > :date")
    <Optional> Page<Newsletter> findNewsletterByCreatedAtAfter(@Param("date") LocalDateTime date, Pageable page);

    void deleteByUser_Id(String userId);
}