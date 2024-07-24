package hng_java_boilerplate.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetUserDto {
    private String name;
    private String id;
    private String email;
    private ProfileDto profile;
    private List<OrganisationDto> organisations;
    private List<ProductDto> products;

    @Data
    @Builder
    public static class ProfileDto {
        private String first_name;
        private String last_name;
        private String phone;
        private String avatar_url;
    }

    @Data
    @Builder
    public static class OrganisationDto {
        private String org_id;
        private String name;
        private String description;
    }

    @Data
    @Builder
    public static class ProductDto {
        private String product_id;
        private String name;
        private String description;
    }
}
