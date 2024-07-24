package hng_java_boilerplate.blog_test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import hng_java_boilerplate.blog.dtos.BlogSearchResponse;
import hng_java_boilerplate.blog.models.Blog;
import hng_java_boilerplate.blog.models.ErrorResponse;
import hng_java_boilerplate.blog.repository.BlogRepository;
import org.typesense.api.Client;
import org.typesense.api.FieldTypes;
import org.typesense.model.CollectionSchema;

import java.time.LocalDate;
import java.util.Collections;
import org.typesense.model.CollectionSchema;
import org.typesense.model.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class BlogSearchE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private Client typesenseClient;

    @Container
    public static final GenericContainer<?> typesense = new GenericContainer<>("typesense/typesense:0.23.1")
        .withExposedPorts(8108)
        .withEnv("TYPESENSE_API_KEY", "xyz");

    @DynamicPropertySource
    static void typesenseProperties(DynamicPropertyRegistry registry) {
        registry.add("typesense.host", typesense::getHost);
        registry.add("typesense.port", () -> typesense.getMappedPort(8108));
        registry.add("typesense.apiKey", () -> "xyz");
    }

    @BeforeEach
    void setUp() throws Exception {
        // Clear existing data
        blogRepository.deleteAll();
        try {
            typesenseClient.collections("blogs").delete();
        } catch (Exception e) {
            // Ignore if collection doesn't exist
        }

        // Create the 'blogs' collection
    CollectionSchema schema = new CollectionSchema();
    schema.setName("blogs");
    schema.setDefaultSortingField("createdDate");

    ArrayList<Field> fields = new ArrayList<>();
    fields.add(new Field().name("author").type(FieldTypes.STRING).facet(true));
    fields.add(new Field().name("title").type(FieldTypes.STRING));
    fields.add(new Field().name("content").type(FieldTypes.STRING));
    fields.add(new Field().name("tags").type(FieldTypes.STRING_ARRAY).facet(true));
    fields.add(new Field().name("createdDate").type(FieldTypes.INT64).facet(true).sort(true));

    schema.setFields(fields);
        
    typesenseClient.collections().create(schema);

        // Create a test blog entry
        Blog blog = new Blog();
        blog.setAuthor("John Doe");
        blog.setTitle("Spring Boot E2E Testing");
        blog.setContent("This is a test blog post for E2E testing.");
        blog.setTags(Collections.singletonList("testing"));
        blog.setCreatedDate(LocalDate.now());

        blogRepository.save(blog);

       // Index the blog in Typesense
    Map<String, Object> document = new HashMap<>();
    document.put("author", blog.getAuthor());
    document.put("title", blog.getTitle());
    document.put("content", blog.getContent());
    document.put("tags", blog.getTags());
    document.put("createdDate", blog.getCreatedDate().atStartOfDay(ZoneId.systemDefault()).toInstant().getEpochSecond());

    typesenseClient.collections("blogs").documents().create(document);
    }

    @Test
    void testSearchBlogs_Success() {
        String url = String.format("http://localhost:%d/api/v1/blogs/search?author=John%%20Doe&title=Spring%%20Boot&content=test&tags=testing&createdDate=%s&page=0&pageSize=10",
                port, LocalDate.now());

        ResponseEntity<BlogSearchResponse> response = restTemplate.getForEntity(url, BlogSearchResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        BlogSearchResponse searchResponse = response.getBody();
        assertNotNull(searchResponse);
        assertEquals(1, searchResponse.getTotalResults());
        assertEquals(1, searchResponse.getBlogs().size());

        Blog foundBlog = searchResponse.getBlogs().get(0);
        assertEquals("John Doe", foundBlog.getAuthor());
        assertEquals("Spring Boot E2E Testing", foundBlog.getTitle());
        assertTrue(foundBlog.getContent().contains("test blog post"));
        assertEquals(Collections.singletonList("testing"), foundBlog.getTags());
        assertEquals(LocalDate.now(), foundBlog.getCreatedDate());
    }

    @Test
    void testSearchBlogs_NotFound() {
        String url = String.format("http://localhost:%d/api/v1/blogs/search?author=Nonexistent%%20Author&page=0&pageSize=10", port);

        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(url, ErrorResponse.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("No blogs found matching the criteria", errorResponse.getMessage());
        assertEquals("Not Found", errorResponse.getError());
        assertEquals(404, errorResponse.getStatusCode());
    }

    @Test
    void testSearchBlogs_BadRequest() {
        String url = String.format("http://localhost:%d/api/v1/blogs/search?page=invalid", port);

        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(url, ErrorResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertTrue(errorResponse.getMessage().contains("Invalid search parameters"));
        assertEquals("Bad Request", errorResponse.getError());
        assertEquals(400, errorResponse.getStatusCode());
    }
}