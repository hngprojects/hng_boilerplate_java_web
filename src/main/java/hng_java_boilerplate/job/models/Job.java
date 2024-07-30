package hng_java_boilerplate.job.models;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String company;
    private String location;
    private double salary;

    
}
