package hng_java_boilerplate.product.dto;

import lombok.Builder;

@Builder
public record ProductCountDto(int status_code, String status, CountData data) {

    @Builder
    public record CountData(int count) {}
}
