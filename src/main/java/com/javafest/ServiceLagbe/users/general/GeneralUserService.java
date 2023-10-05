package com.javafest.ServiceLagbe.users.general;

import com.javafest.ServiceLagbe.records.GeneralRegistrationRequest;
import com.javafest.ServiceLagbe.users.worker.WorkerUser;
import com.javafest.ServiceLagbe.users.worker.WorkerUserRepository;
import com.javafest.ServiceLagbe.verification.VerificationToken;
import com.javafest.ServiceLagbe.verification.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class GeneralUserService implements IGeneralUserService {

    private final GeneralUserRepository generalUserRepository;
    private final WorkerUserRepository workerUserRepository;
    private final VerificationTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public GeneralUser register(GeneralRegistrationRequest request) {
        Optional<GeneralUser> user = generalUserRepository.findByEmail(request.email());
        if (user.isPresent()){
            return new GeneralUser();
        }
        var newUser = new GeneralUser();
        newUser.setFirstName(request.firstName());
        newUser.setLastName(request.lastName());
        newUser.setEmail(request.email());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setRole("GENERAL");
        return generalUserRepository.save(newUser);
    }
    @Override
    public void saveUserVerificationToken(GeneralUser theUser, String token) {
        var verificationToken = new VerificationToken(token, theUser);
        tokenRepository.save(verificationToken);
    }

    @Override
    public String validateToken(String theToken) {
        VerificationToken token = tokenRepository.findByToken(theToken);
        if(token==null){
            return "Invalid Verification token";
        }
        GeneralUser user = token.getUser();
        Calendar calendar = Calendar.getInstance();
        if((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
            tokenRepository.delete(token);
            generalUserRepository.delete(user);
            return "Token already expired";
        }
        user.setEnable(true);
        generalUserRepository.save(user);
        return "valid";
    }

    @Override
    public List<WorkerUser> getWorkers(String keyword) {
        Predicate<? super WorkerUser> predicate = user -> user.getIsAvailable().equalsIgnoreCase("Yes");
        if (keyword!=null) {
            return workerUserRepository.findAll(keyword).stream().filter(predicate).toList();
        }
        return workerUserRepository.findAll().stream().filter(predicate).toList();
    }

    @Override
    public GeneralUser findByEmail(String email) {
        Optional<GeneralUser> optionalUser = generalUserRepository.findByEmail(email);
        return optionalUser.get();
    }
    @Override
    public void updateUserVerificationToken(GeneralUser user, String token) {
        VerificationToken verificationToken = tokenRepository.findByUser(user);
        verificationToken.setToken(token);
        tokenRepository.save(verificationToken);
    }
    @Override
    public void resetPassword(String password, GeneralUser user) {
        user.setPassword(passwordEncoder.encode(password));
        generalUserRepository.save(user);
    }
}
