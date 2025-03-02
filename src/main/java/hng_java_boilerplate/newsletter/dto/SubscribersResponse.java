package hng_java_boilerplate.newsletter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SubscribersResponse {
    private List<SubscribersDto> subscribers;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
