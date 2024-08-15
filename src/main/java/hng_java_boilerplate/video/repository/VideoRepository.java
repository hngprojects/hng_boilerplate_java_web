package hng_java_boilerplate.video.repository;

import hng_java_boilerplate.video.entity.VideoSuite;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<VideoSuite,String> {

    @Modifying
    @Transactional
    @Query("UPDATE VideoSuite vs SET vs.filename = :filename, " +
            "vs.progress = :progress, vs.currentProcess = :currentProcess WHERE vs.jobId = :jobId")
    void updateJob(@Param("jobId") String jobId,
                   @Param("filename") String filename,
                   @Param("progress") int progress,
                   @Param("currentProcess") String currentProcess);
}
