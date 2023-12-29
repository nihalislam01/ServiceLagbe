package com.javafest.ServiceLagbe.users.worker;

import com.javafest.ServiceLagbe.records.WorkerRegistrationRequest;
import com.javafest.ServiceLagbe.verification.VerificationOTP;
import com.javafest.ServiceLagbe.verification.VerificationOTPRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkerUserService implements IWorkerUserService {

    private final WorkerUserRepository workerUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationOTPRepository otpRepository;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    public WorkerUser register(WorkerRegistrationRequest request) {
        Optional<WorkerUser> user = workerUserRepository.findByNumber(request.number());
        if (user.isPresent()){
            return new WorkerUser();
        }
        var newUser = new WorkerUser();
        newUser.setRating("0");
        newUser.setPersonRated(0);
        newUser.setTotalRating(0);
        newUser.setEnable(true);
        newUser.setFirstName(request.firstName());
        newUser.setLastName(request.lastName());
        newUser.setPin(passwordEncoder.encode(request.pin()));
        newUser.setLocation(request.location());
        newUser.setRole("WORKER");
        newUser.setPosts(request.posts());
        newUser.setIsAvailable(request.isAvailable());
        newUser.setNumber(request.number());
        return workerUserRepository.save(newUser);
    }

    @Override
    public WorkerUser findByNumber(String number) {
        Optional<WorkerUser> optionalUser = workerUserRepository.findByNumber(number);
        return optionalUser.get();
    }

    @Override
    public void saveUserVerificationOTP(WorkerUser theUser, String otp) {
        var verificationOTP = new VerificationOTP(otp, theUser);
        otpRepository.save(verificationOTP);
    }

    @Override
    public String validateOTP(String theOTP) {
        VerificationOTP otp = otpRepository.findByOtp(theOTP);
        if(otp==null){
            return "Invalid Verification OTP";
        }
        WorkerUser user = otp.getUser();
        Calendar calendar = Calendar.getInstance();
        if((otp.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
            otpRepository.delete(otp);
            workerUserRepository.delete(user);
            return "OTP already expired";
        }
        user.setEnable(true);
        workerUserRepository.save(user);
        return "valid";
    }
    @Override
    public void setIsAvailable(String isAvailable, WorkerUser user) {
        user.setIsAvailable(isAvailable);
        workerUserRepository.save(user);
    }
    @Override
    public void rateWorker(long rate, WorkerUser worker) {
        worker.setTotalRating(worker.getTotalRating()+rate);
        worker.setPersonRated(worker.getPersonRated()+1);
        workerUserRepository.save(worker);
        double rating = (double) worker.getTotalRating()/worker.getPersonRated();
        worker.setRating(df.format(rating));
        workerUserRepository.save(worker);
    }
    @Override
    public void resetPassword(String password, WorkerUser user) {
        user.setPin(passwordEncoder.encode(password));
        workerUserRepository.save(user);
    }
    @Override
    public void updateUserVerificationOTP(WorkerUser user, String otp) {
        VerificationOTP verificationOTP = otpRepository.findByUser(user).get();
        verificationOTP.setOtp(otp);
        otpRepository.save(verificationOTP);

    }
}
