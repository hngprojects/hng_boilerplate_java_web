package hng_java_boilerplate.externalPage.topic.service;

import hng_java_boilerplate.exception.exception_class.NotFoundException;
import hng_java_boilerplate.externalPage.topic.entity.Topic;
import hng_java_boilerplate.externalPage.topic.repository.TopicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TopicServiceTest {

    @Mock
    private TopicRepository topicRepository;

    @InjectMocks
    private TopicService topicService;

    private Topic topic;
    private UUID id;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = UUID.randomUUID();
        topic = new Topic();
        topic.setId(id);
        topic.setTitle("Test Topic");
        topic.setContent("Test Content");
        topic.setAuthor("Test Author");
    }

    @Test
    void createTopic_ShouldReturnSavedTopic() {
        when(topicRepository.save(any(Topic.class))).thenReturn(topic);

        Topic result = topicService.createTopic(topic);

        assertNotNull(result);
        assertEquals(topic.getTitle(), result.getTitle());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
        verify(topicRepository).save(any(Topic.class));
    }

    @Test
    void getAllTopics_ShouldReturnListOfTopics() {
        List<Topic> topics = Arrays.asList(topic, new Topic());
        when(topicRepository.findAll()).thenReturn(topics);

        List<Topic> result = topicService.getAllTopics();

        assertEquals(2, result.size());
        verify(topicRepository).findAll();
    }

    @Test
    void deleteTopic_ShouldCallRepositoryDeleteMethod() {
        topicService.deleteTopic(id);

        verify(topicRepository).deleteById(id);
    }

    @Test
    void existsById_ShouldReturnTrue_WhenTopicExists() {
        when(topicRepository.existsById(id)).thenReturn(true);

        assertTrue(topicService.existsById(id));
        verify(topicRepository).existsById(id);
    }

    @Test
    void existsById_ShouldReturnFalse_WhenTopicDoesNotExist() {
        when(topicRepository.existsById(id)).thenReturn(false);

        assertFalse(topicService.existsById(id));
        verify(topicRepository).existsById(id);
    }

    @Test
    void getTopicById_ShouldReturnTopic_WhenTopicExists() {
        when(topicRepository.findById(id)).thenReturn(Optional.of(topic));

        Optional<Topic> result = topicService.getTopicById(id);

        assertTrue(result.isPresent());
        assertEquals(topic, result.get());
        verify(topicRepository).findById(id);
    }

    @Test
    void getTopicById_ShouldReturnEmptyOptional_WhenTopicDoesNotExist() {
        when(topicRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Topic> result = topicService.getTopicById(id);

        assertFalse(result.isPresent());
        verify(topicRepository).findById(id);
    }

    @Test
    void updateTopic_ShouldReturnUpdatedTopic_WhenTopicExists() {
        Topic updatedTopic = new Topic();
        updatedTopic.setTitle("Updated Title");
        updatedTopic.setContent("Updated Content");
        updatedTopic.setAuthor("Updated Author");

        when(topicRepository.findById(id)).thenReturn(Optional.of(topic));
        when(topicRepository.save(any(Topic.class))).thenAnswer(invocation -> {
            Topic savedTopic = invocation.getArgument(0);
            savedTopic.setUpdatedAt(LocalDateTime.now());
            return savedTopic;
        });

        Topic result = topicService.updateTopic(id, updatedTopic);

        assertNotNull(result);
        assertEquals(updatedTopic.getTitle(), result.getTitle());
        assertEquals(updatedTopic.getContent(), result.getContent());
        assertEquals(updatedTopic.getAuthor(), result.getAuthor());
        assertNotNull(result.getUpdatedAt());
        verify(topicRepository).findById(id);
        verify(topicRepository).save(any(Topic.class));
    }

    @Test
    void updateTopic_ShouldThrowResourceNotFoundException_WhenTopicDoesNotExist() {
        when(topicRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> topicService.updateTopic(id, new Topic()));
        verify(topicRepository).findById(id);
    }

    @Test
    void searchTopicsByTitle_ShouldReturnMatchingTopics() {
        String searchTitle = "Test";
        List<Topic> matchingTopics = Arrays.asList(topic, new Topic());
        when(topicRepository.findByTitleContainingIgnoreCase(searchTitle)).thenReturn(matchingTopics);

        List<Topic> result = topicService.searchTopicsByTitle(searchTitle);

        assertEquals(2, result.size());
        verify(topicRepository).findByTitleContainingIgnoreCase(searchTitle);
    }
}