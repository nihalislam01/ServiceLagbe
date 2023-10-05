package com.javafest.ServiceLagbe.verification;

import com.javafest.ServiceLagbe.users.worker.WorkerUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class VerificationOTP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String otp;
    private Date expirationTime;
    private static final int EXPIRATION_TIME = 15;

    @OneToOne
    @JoinColumn(name = "user_id")
    private WorkerUser user;

    public VerificationOTP(String otp, WorkerUser user) {
        super();
        this.otp = otp;
        this.user = user;
        this.expirationTime = this.getTokenExpirationTime();
    }

    public VerificationOTP(String otp) {
        super();
        this.otp = otp;
        this.expirationTime = this.getTokenExpirationTime();
    }

    private Date getTokenExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, EXPIRATION_TIME);
        return new Date(calendar.getTime().getTime());
    }
}
