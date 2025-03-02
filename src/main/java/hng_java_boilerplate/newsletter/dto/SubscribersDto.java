package hng_java_boilerplate.newsletter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SubscribersDto {
    private String id;
    private String email;
    private LocalDateTime subscribedAt;
}
