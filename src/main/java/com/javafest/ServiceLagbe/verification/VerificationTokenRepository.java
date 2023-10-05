package com.javafest.ServiceLagbe.verification;

import com.javafest.ServiceLagbe.users.general.GeneralUser;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
    VerificationToken findByUser(GeneralUser user);
}
