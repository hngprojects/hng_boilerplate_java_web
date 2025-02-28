package hng_java_boilerplate.newsletter.repository;

import hng_java_boilerplate.newsletter.entity.Newsletter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface NewsletterRepository extends JpaRepository<Newsletter, String> {

    @Query("SELECT n FROM Newsletter n WHERE n.userId = :userId")
    <Optional> List<Newsletter> findNewsletterByUserId(@Param("userId") String userId);

    @Query("SELECT n FROM Newsletter n WHERE n.createdAt > :date")
    <Optional> List<Newsletter> findNewsletterByCreatedAtAfter(@Param("date") LocalDateTime date);

    @Modifying
    @Query("DELETE FROM Newsletter n WHERE n.userId = :userId")
    void deleteNewsletterByUserId(@Param("userId") String userId);
}