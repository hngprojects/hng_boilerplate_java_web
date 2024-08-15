package hng_java_boilerplate.resources.repository;

import hng_java_boilerplate.resources.entity.Resources;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<Resources, String> {

        @Query("SELECT r FROM Resources r WHERE :query IS NULL OR LOWER(r.title) " +
                "LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(r.description) LIKE LOWER(CONCAT('%', :query, '%'))")
        Page<Resources> search(
                @Param("query") String query, Pageable pageable);



}
