package hng_java_boilerplate.plans.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class PlanObjectResponse<T> {

    private String status;
    private String message;
    private T data;

}
