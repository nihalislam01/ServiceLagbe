package com.javafest.ServiceLagbe.users.general;

import com.javafest.ServiceLagbe.records.GeneralRegistrationRequest;
import com.javafest.ServiceLagbe.users.worker.WorkerUser;

import java.util.List;
import java.util.Optional;

public interface IGeneralUserService {
    public GeneralUser register(GeneralRegistrationRequest request);
    public void saveUserVerificationToken(GeneralUser theUser, String token);
    public String validateToken(String theToken);
    public List<WorkerUser> getWorkers(String keyword);

    public GeneralUser findByEmail(String email);
    public void updateUserVerificationToken(GeneralUser user, String token);
    public void resetPassword(String password, GeneralUser user);
}
