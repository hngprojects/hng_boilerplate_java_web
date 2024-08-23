package hng_java_boilerplate.externalPage.jobs.repository;

import hng_java_boilerplate.externalPage.jobs.entity.JobListing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobListingRepository extends JpaRepository<JobListing, Long> {
}
