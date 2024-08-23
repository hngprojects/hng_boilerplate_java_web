package hng_java_boilerplate.messaging.email.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private String to;
    private String subject;
    private String text;
}
