package hng_java_boilerplate.user.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class VerificationToken {
    private static final int EXPIRATION_TIME = 60;

    @Id
    private String id;

    private String token;

    private Date expirationTime;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "FK_USER_VERIFY_TOKEN"))
    private User user;

    public VerificationToken(User user, String token){
        super();
        this.token = token;
        this.user = user;
        this.expirationTime = calculateExpirationDate(EXPIRATION_TIME);
    }
    public VerificationToken(String token){
        super();
        this.token = token;
        this.expirationTime = calculateExpirationDate(EXPIRATION_TIME);
    }

    private Date calculateExpirationDate(int expirationTime) {
        Calendar calender = Calendar.getInstance();
        calender.setTimeInMillis(new Date().getTime());
        calender.add(Calendar.MINUTE, expirationTime);
        return new Date(calender.getTime().getTime());
    }
}
