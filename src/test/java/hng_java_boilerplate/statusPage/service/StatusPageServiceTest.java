package hng_java_boilerplate.statusPage.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hng_java_boilerplate.statusPage.entity.StatusPage;
import hng_java_boilerplate.statusPage.repository.StatusPageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class StatusPageServiceTest {

    @Mock
    private StatusPageRepository statusPageRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private StatusPageServiceImpl statusPageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateApiStatus() throws IOException {
        // Prepare test data
        JsonNode mockJsonNode = mock(JsonNode.class);
        JsonNode mockRunNode = mock(JsonNode.class);
        JsonNode mockExecutionsNode = mock(JsonNode.class);
        JsonNode mockExecutionNode = mock(JsonNode.class);
        JsonNode mockItemNode = mock(JsonNode.class);
        JsonNode mockResponseNode = mock(JsonNode.class);

        when(objectMapper.readTree(any(File.class))).thenReturn(mockJsonNode);
        when(mockJsonNode.get("run")).thenReturn(mockRunNode);
        when(mockRunNode.get("executions")).thenReturn(mockExecutionsNode);
        when(mockExecutionsNode.iterator()).thenReturn(Arrays.asList(mockExecutionNode).iterator());

        // Mock the structure for a single execution
        when(mockExecutionNode.get("item")).thenReturn(mockItemNode);
        when(mockExecutionNode.get("response")).thenReturn(mockResponseNode);
        when(mockItemNode.get("name")).thenReturn(mock(JsonNode.class));
        when(mockItemNode.get("name").asText()).thenReturn("Test API");
        when(mockResponseNode.get("responseTime")).thenReturn(mock(JsonNode.class));
        when(mockResponseNode.get("responseTime").asInt()).thenReturn(100);
        when(mockResponseNode.get("code")).thenReturn(mock(JsonNode.class));
        when(mockResponseNode.get("code").asInt()).thenReturn(200);
        when(mockResponseNode.get("status")).thenReturn(mock(JsonNode.class));
        when(mockResponseNode.get("status").asText()).thenReturn("OK");

        // Call the method
        statusPageService.updateApiStatus();

        // Verify that processJsonData was called and a StatusPage was saved
        verify(statusPageRepository, times(1)).save(argThat(statusPage ->
                statusPage.getApiGroup().equals("Test API") &&
                        statusPage.getResponseTime() == 100 &&
                        statusPage.getStatus() == StatusPage.Status.OPERATIONAL &&
                        statusPage.getDetails().equals("OK")
        ));
    }

    @Test
    void testGetAllApiStatuses() {
        // Prepare test data
        List<StatusPage> mockStatusPages = Arrays.asList(new StatusPage(), new StatusPage());
        when(statusPageRepository.findAll()).thenReturn(mockStatusPages);

        // Call the method
        List<StatusPage> result = statusPageService.getAllApiStatuses();

        // Verify the result
        assertEquals(mockStatusPages, result);
        verify(statusPageRepository, times(1)).findAll();
    }
}