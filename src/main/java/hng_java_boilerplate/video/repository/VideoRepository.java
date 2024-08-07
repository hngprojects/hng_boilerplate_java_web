package hng_java_boilerplate.video.repository;

import hng_java_boilerplate.product.entity.Product;
import hng_java_boilerplate.video.entity.VideoSuite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VideoRepository extends JpaRepository<VideoSuite,String> {

}
