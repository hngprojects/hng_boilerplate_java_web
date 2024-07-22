package hng_java_boilerplate.SMS.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.Instant;
import java.util.Date;

@Entity
public class SMS {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String destination_phone_number;
    private String message;
    private String sender_id;
    private Instant created_at;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDestination_phone_number() {
        return destination_phone_number;
    }
    public void setDestination_phone_number(String destination_phone_number) {
        this.destination_phone_number = destination_phone_number;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getSender_id() {
        return sender_id;
    }
    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }
    public Instant getCreated_at() {
        return created_at;
    }
    public void setCreated_at(Instant created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "SMS{" +
                "id=" + id +
                ", destination_phone_number='" + destination_phone_number + '\'' +
                ", message='" + message + '\'' +
                ", sender_id='" + sender_id + '\'' +
                ", created_at=" + created_at +
                '}';
    }
}
