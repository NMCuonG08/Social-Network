package com.example.Social_Network.User.Registration;


import com.example.Social_Network.User.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

@Entity
@Setter
@Getter
@NoArgsConstructor

public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private static final int EXPIRATION_TIME = 60;
    private Date expirationTime;

    @OneToOne
    @JoinColumn(name = "userID")
    private User user;

    public VerificationToken(String token, Date expirationTime, User user) {
        super();
        this.token = token;
        this.expirationTime = this.getExpirationTime();
        this.user = user;
    }

    public VerificationToken(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public Date getExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, EXPIRATION_TIME);
        return new Date(calendar.getTime().getTime());
    }



}
