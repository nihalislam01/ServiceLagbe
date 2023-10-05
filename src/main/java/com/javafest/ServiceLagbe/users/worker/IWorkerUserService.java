package com.javafest.ServiceLagbe.users.worker;

import com.javafest.ServiceLagbe.records.WorkerRegistrationRequest;
import com.javafest.ServiceLagbe.users.general.GeneralUser;

public interface IWorkerUserService {
    public WorkerUser register(WorkerRegistrationRequest request);
    public WorkerUser findByNumber(String number);
    public void saveUserVerificationOTP(WorkerUser theUser, String otp);
    public String validateOTP(String theOTP);
    public void setIsAvailable(String isAvailable, WorkerUser user);
    public void rateWorker(long rate, WorkerUser worker);
    public void resetPassword(String password, WorkerUser user);
    public void updateUserVerificationOTP(WorkerUser user, String otp);
}
