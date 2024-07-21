package hng_java_boilerplate.region.repository;


import hng_java_boilerplate.region.entity.RegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RegionRepository extends JpaRepository<RegionEntity, String> {
}

