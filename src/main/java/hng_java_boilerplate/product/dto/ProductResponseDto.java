package hng_java_boilerplate.product.dto;

import hng_java_boilerplate.user.entity.User;

public record ProductResponseDto(
        String name,
        String description,
        User user
) {
}
