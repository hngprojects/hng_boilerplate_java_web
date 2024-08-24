package hng_java_boilerplate.user.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class PasswordResetToken {

    private static final int EXPIRATION_TIME = 20;

    @Id
    private String id;

    private String token;

    private Date expirationTime;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "FK_USER_PASSWORD_TOKEN"))
    private User user;

    public PasswordResetToken(User user, String token) {
        super();
        this.id = UUID.randomUUID().toString();
        this.token = token;
        this.user = user;
        this.expirationTime = calculateExpirationDate(EXPIRATION_TIME);
    }

    public PasswordResetToken(String token) {
        super();
        this.id = UUID.randomUUID().toString();
        this.token = token;
        this.expirationTime = calculateExpirationDate(EXPIRATION_TIME);
    }

    private Date calculateExpirationDate(int expirationTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, expirationTime);
        return new Date(calendar.getTime().getTime());
    }
}