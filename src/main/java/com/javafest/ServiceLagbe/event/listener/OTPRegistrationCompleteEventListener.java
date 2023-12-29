package com.javafest.ServiceLagbe.event.listener;

import com.javafest.ServiceLagbe.event.OTPRegistrationCompleteEvent;
import com.javafest.ServiceLagbe.users.worker.WorkerUser;
import com.javafest.ServiceLagbe.users.worker.WorkerUserService;
import com.javafest.ServiceLagbe.verification.TwilioConfiguration;
import com.javafest.ServiceLagbe.verification.VerificationOTP;
import com.javafest.ServiceLagbe.verification.VerificationOTPRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
public class OTPRegistrationCompleteEventListener implements ApplicationListener<OTPRegistrationCompleteEvent> {
    private final WorkerUserService userService;
    private final TwilioConfiguration twilioConfiguration;
    private final VerificationOTPRepository otpRepository;
    private WorkerUser theUser;
    @Override
    public void onApplicationEvent(OTPRegistrationCompleteEvent event) {
        Twilio.init(twilioConfiguration.getAccountSid(), twilioConfiguration.getAuthToken());
        theUser = event.getUser();
        String verificationOTP = generateOTP();
        if (!theUser.isEnable()) {
            userService.saveUserVerificationOTP(theUser, verificationOTP);
        } else {
            userService.updateUserVerificationOTP(theUser, verificationOTP);
        }
        try {
            sendVerificationMessage(verificationOTP);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendVerificationMessage(String verificationOTP) throws MessagingException, UnsupportedEncodingException {
        PhoneNumber to = new PhoneNumber("+88" + theUser.getNumber());
        PhoneNumber from = new PhoneNumber(twilioConfiguration.getTrialNumber());
        String otpMessage = "Dear Customer , Your OTP is " + verificationOTP + ". Use this Passcode to complete your transaction. Thank You.";
        Message message = Message
                .creator(to, from,
                        otpMessage)
                .create();
    }
    private String generateOTP() {
        return new DecimalFormat("000000")
                .format(new Random().nextInt(999999));
    }
}
