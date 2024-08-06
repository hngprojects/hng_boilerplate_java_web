package hng_java_boilerplate.video.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "video_suite")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
public class VideoSuite {
    @Id
    @Column(name = "job_id", updatable = false, nullable = false)
    private String jobId;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "job_type", nullable = false)
    private String jobType;

    @Column(name = "output_video_url")
    private String outputVideoUrl;

    @Column(name = "message")
    private String message;

    @Column(name = "progress")
    private int progress;
}

