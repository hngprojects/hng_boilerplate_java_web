package hng_java_boilerplate.newsletter.dto;

import lombok.Builder;

@Builder
public record UnsubscribeResponse(int status_code,String message) {
}