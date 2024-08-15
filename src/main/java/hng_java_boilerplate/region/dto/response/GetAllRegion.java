package hng_java_boilerplate.region.dto.response;

import java.util.List;

public record GetAllRegion(int status, String message, List<GetResponse> data) {
}
