package hng_java_boilerplate.organisation.common;

import lombok.Builder;

import java.util.List;

@Builder
public record PageResponse <T> (
        String status,
        String message,
        List<T> users,
        Integer status_code,
        int number,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
){
}