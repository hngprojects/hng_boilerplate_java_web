package hng_java_boilerplate.helpCenter.topic.controller;

import hng_java_boilerplate.exception.ErrorResponseDto;
import hng_java_boilerplate.exception.ValidationError;
import hng_java_boilerplate.helpCenter.topic.entity.Topic;
import hng_java_boilerplate.helpCenter.topic.service.TopicService;
import hng_java_boilerplate.region.dto.response.GetResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Topic Controller", description = "Controller for Topic")
public class TopicController {

    private final TopicService topicService;

    @PostMapping
    @Secured("ROLE_ADMIN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "create a topic success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "User not authenticated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(responseCode = "403", description = "user not admin",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(responseCode = "422", description = "Invalid or missing required request data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
    })
    @Operation(summary = "create a topic")
    public ResponseEntity<?> createTopic(@RequestBody Topic topic) {
        if (topic.getTitle() == null || topic.getContent() == null || topic.getAuthor() == null) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorResponse(false, "Invalid input data", 422));
        }

        Topic createdTopic = topicService.createTopic(topic);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>(true, createdTopic, 201));
    }


    @GetMapping
    @Secured("ROLE_ADMIN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "get all topics",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AllTopicsResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "User not authenticated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(responseCode = "403", description = "user not admin",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
    })
    @Operation(summary = "User with admin privilege gets all topics")
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "get a topic by id success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "User not authenticated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Topic not found with id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
    })
    @Operation(summary = "create a region", description = "Creates a region for the logged in user")
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "delete a topic",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "User not authenticated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(responseCode = "403", description = "user not admin",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "topic do not exist with id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
    })
    @Operation(summary = "create a region", description = "Creates a region for the logged in user")
    public ResponseEntity<?> deleteTopic(@PathVariable UUID id) {
        if (!topicService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(false, "Topic not found, please check and try again", 404));
        }

        topicService.deleteTopic(id);
        return ResponseEntity.ok(new SuccessResponse<>(true, "Topic deleted successfully", 200));
    }


    @PutMapping("/{id}")
    @Secured("ROLE_ADMIN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "update a topic",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "User not authenticated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(responseCode = "403", description = "user not admin",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "topic do not exist with id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "422", description = "Invalid or missing required request data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
    })
    @Operation(description = "User with admin privilege update a topic using the ID of the topic")
    public ResponseEntity<?> updateTopic(@PathVariable UUID id, @RequestBody Topic topicDetails) {
        if (topicDetails.getTitle() == null || topicDetails.getContent() == null || topicDetails.getAuthor() == null) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorResponse(false, "Invalid input data", 422));
        }

        Topic updatedTopic = topicService.updateTopic(id, topicDetails);
        return ResponseEntity.ok(new SuccessResponse<>(true, updatedTopic, 200));
    }


    @GetMapping("/search")
    @ApiResponse(responseCode = "200", description = "search a topic by title success",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SearchTopicsResponse.class))
    )
    @Operation(description = "search a topic by passing the topic title as request parameter")
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

