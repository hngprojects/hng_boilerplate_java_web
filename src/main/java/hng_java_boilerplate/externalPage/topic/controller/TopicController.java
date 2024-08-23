package hng_java_boilerplate.externalPage.topic.controller;

import hng_java_boilerplate.externalPage.topic.entity.Topic;
import hng_java_boilerplate.externalPage.topic.service.TopicService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/help-center/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> createTopic(@RequestBody Topic topic) {
        if (topic.getTitle() == null || topic.getContent() == null || topic.getAuthor() == null) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorResponse(false, "Invalid input data", 422));
        }

        Topic createdTopic = topicService.createTopic(topic);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse(true, createdTopic, 201));
    }

    @GetMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> getAllTopics() {
        List<Topic> topics = topicService.getAllTopics();
        List<TopicResponse> topicResponses = topics.stream().map(topic -> new TopicResponse(
                topic.getId(),
                topic.getTitle(),
                topic.getContent()
        )).collect(Collectors.toList());

        return ResponseEntity.ok(new AllTopicsResponse(true, "Topics retrieved successfully", topicResponses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTopicById(@PathVariable UUID id) {
        Optional<Topic> topic = topicService.getTopicById(id);
        if (topic.isPresent()) {
            TopicResponse topicResponse = new TopicResponse(
                    topic.get().getId(),
                    topic.get().getTitle(),
                    topic.get().getContent()
            );
            return ResponseEntity.ok(new SuccessResponse<>(true, topicResponse, 200));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(false, "Topic not found", 404));
        }
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> deleteTopic(@PathVariable UUID id) {
        if (!topicService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(false, "Topic not found, please check and try again", 404));
        }

        try {
            topicService.deleteTopic(id);
            return ResponseEntity.ok(new SuccessResponse<>(true, "Topic deleted successfully", 200));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(false, "Something went wrong, please try again later or contact support if it persists", 500));
        }
    }

    @PutMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> updateTopic(@PathVariable UUID id, @RequestBody Topic topicDetails) {
        if (topicDetails.getTitle() == null || topicDetails.getContent() == null || topicDetails.getAuthor() == null) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorResponse(false, "Invalid input data", 422));
        }

        Topic updatedTopic = topicService.updateTopic(id, topicDetails);
        return ResponseEntity.ok(new SuccessResponse<>(true, updatedTopic, 200));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchTopics(@RequestParam String title) {
        List<Topic> topics = topicService.searchTopicsByTitle(title);
        if (topics.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(false, "No article matches the title search param", 404));
        }
        List<TopicResponse> topicResponses = topics.stream().map(topic -> new TopicResponse(
                topic.getId(),
                topic.getTitle(),
                topic.getContent()
        )).collect(Collectors.toList());

        return ResponseEntity.ok(new SearchTopicsResponse(true, "Topics retrieved successfully", 200, topicResponses));
    }

    @Getter
    @Setter
    public static class ErrorResponse {
        private boolean success;
        private String message;
        private int statusCode;

        public ErrorResponse(boolean success, String message, int statusCode) {
            this.success = success;
            this.message = message;
            this.statusCode = statusCode;
        }
    }

    @Getter
    @Setter
    public static class SuccessResponse<T> {
        private boolean success;
        private T data;
        private int statusCode;

        public SuccessResponse(boolean success, T data, int statusCode) {
            this.success = success;
            this.data = data;
            this.statusCode = statusCode;
        }
    }

    @Getter
    @Setter
    public static class TopicResponse {
        private UUID articleId;
        private String title;
        private String content;

        public TopicResponse(UUID articleId, String title, String content) {
            this.articleId = articleId;
            this.title = title;
            this.content = content;
        }
    }

    @Getter
    @Setter
    public static class AllTopicsResponse {
        private boolean success;
        private String message;
        private List<TopicResponse> topics;

        public AllTopicsResponse(boolean success, String message, List<TopicResponse> topics) {
            this.success = success;
            this.message = message;
            this.topics = topics;
        }
    }

    @Getter
    @Setter
    public static class SearchTopicsResponse {
        private boolean success;
        private String message;
        private int statusCode;
        private List<TopicResponse> topics;

        public SearchTopicsResponse(boolean success, String message, int statusCode, List<TopicResponse> topics) {
            this.success = success;
            this.message = message;
            this.statusCode = statusCode;
            this.topics = topics;
        }
    }
}

