package hng_java_boilerplate.externalPage.topic.service;

import hng_java_boilerplate.exception.exception_class.NotFoundException;
import hng_java_boilerplate.externalPage.topic.entity.Topic;
import hng_java_boilerplate.externalPage.topic.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;

    public Topic createTopic(Topic topic) {
        topic.setCreatedAt(LocalDateTime.now());
        topic.setUpdatedAt(LocalDateTime.now());
        return topicRepository.save(topic);
    }

    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    public void deleteTopic(UUID id) {
        topicRepository.deleteById(id);
    }

    public boolean existsById(UUID id) {
        return topicRepository.existsById(id);
    }

    public Optional<Topic> getTopicById(UUID id) {
        return topicRepository.findById(id);
    }

    public Topic updateTopic(UUID id, Topic topicDetails) {
        Topic topic = topicRepository.findById(id).orElseThrow(() -> new NotFoundException("Topic not found"));
        topic.setTitle(topicDetails.getTitle());
        topic.setContent(topicDetails.getContent());
        topic.setAuthor(topicDetails.getAuthor());
        topic.setUpdatedAt(LocalDateTime.now());
        return topicRepository.save(topic);
    }

    public List<Topic> searchTopicsByTitle(String title) {
        return topicRepository.findByTitleContainingIgnoreCase(title);
    }
}
