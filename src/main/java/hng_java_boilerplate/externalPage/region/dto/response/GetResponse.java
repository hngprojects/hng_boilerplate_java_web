package hng_java_boilerplate.externalPage.region.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetResponse {
    private String id;
    private String user_id;
    private String region;
    private String language;
    private String timezone;
}
