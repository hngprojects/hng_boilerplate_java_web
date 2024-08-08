package hng_java_boilerplate.jobs.repository;

import hng_java_boilerplate.jobs.entity.JobListing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobListingRepository extends JpaRepository<JobListing, Long> {
}
