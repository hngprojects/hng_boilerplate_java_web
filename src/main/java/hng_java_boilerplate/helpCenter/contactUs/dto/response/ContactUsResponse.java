package hng_java_boilerplate.helpCenter.contactUs.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactUsResponse {
    private String name;
    private String email;
    private String phone;
    private String message;
}
