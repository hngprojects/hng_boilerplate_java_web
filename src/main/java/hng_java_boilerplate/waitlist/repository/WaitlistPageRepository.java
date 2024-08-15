package hng_java_boilerplate.waitlist.repository;

import hng_java_boilerplate.waitlist.entity.WaitlistPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WaitlistPageRepository extends JpaRepository<WaitlistPage, UUID>, JpaSpecificationExecutor<WaitlistPage> {
}