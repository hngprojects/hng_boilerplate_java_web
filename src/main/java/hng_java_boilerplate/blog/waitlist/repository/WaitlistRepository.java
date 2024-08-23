package hng_java_boilerplate.blog.waitlist.repository;

import hng_java_boilerplate.blog.waitlist.entity.Waitlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WaitlistRepository extends JpaRepository<Waitlist, UUID> {
}
