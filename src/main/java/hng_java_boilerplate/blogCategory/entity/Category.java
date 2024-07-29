package hng_java_boilerplate.blogCategory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category")
public class Category {
    @Id
    private String id;

    private String name;

//    private String testCategory;

//    public Category(String id, String name) {
//        this.id = id;
//        this.name = name;
//        this.testCategory = testCategory;
//    }




    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }

}

