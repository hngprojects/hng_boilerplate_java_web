package hng_java_boilerplate.externalPage.region.dto.response;

import java.util.List;

public record GetAllRegion(int status, String message, List<GetResponse> data) {
}
