package hng_java_boilerplate.image.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@NoArgsConstructor
@Entity
@Table(name ="image")
public class Image {

    @Id
    private String id = UUID.randomUUID().toString();

    private String name;
    private String type;
    private long size;
    private String path;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
