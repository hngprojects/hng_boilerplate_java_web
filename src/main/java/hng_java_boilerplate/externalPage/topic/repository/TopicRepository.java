package hng_java_boilerplate.externalPage.topic.repository;

import hng_java_boilerplate.externalPage.topic.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TopicRepository extends JpaRepository<Topic, UUID> {
    List<Topic> findByTitleContainingIgnoreCase(String title);
}

