package hng_java_boilerplate.region.repository;
import hng_java_boilerplate.region.entity.UserRegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRegionRepository extends JpaRepository<UserRegionEntity, Integer> {
    Optional<UserRegionEntity> findByUserId(UUID userId);
}