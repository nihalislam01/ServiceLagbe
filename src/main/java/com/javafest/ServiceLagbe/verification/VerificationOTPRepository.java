package com.javafest.ServiceLagbe.verification;

import com.javafest.ServiceLagbe.users.worker.WorkerUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationOTPRepository extends JpaRepository<VerificationOTP, Long> {
    VerificationOTP findByOtp(String otp);
    Optional<VerificationOTP> findByUser(WorkerUser user);
}
