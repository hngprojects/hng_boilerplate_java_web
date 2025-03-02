package hng_java_boilerplate.twilio.CallLogs;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@jakarta.persistence.Entity
@Table(name = "call_logs")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String toNumber;
    private String fromNumber;
    private String callSid;
    private LocalDateTime timestamp;
}
