package hng_java_boilerplate.job.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import hng_java_boilerplate.job.models.Job;

public interface JobRepository extends JpaRepository<Job, Long> {
}
