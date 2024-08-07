package hng_java_boilerplate.image.repository;

import hng_java_boilerplate.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image,String> {

}
