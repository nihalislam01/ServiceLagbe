package com.javafest.ServiceLagbe.event.listener;

import com.javafest.ServiceLagbe.event.ResetPasswordViaEmailEvent;
import com.javafest.ServiceLagbe.users.general.GeneralUser;
import com.javafest.ServiceLagbe.users.general.GeneralUserService;
import com.javafest.ServiceLagbe.verification.EmailConfiguration;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResetPasswordViaEmailEventListener implements ApplicationListener<ResetPasswordViaEmailEvent> {

    private final GeneralUserService userService;
    private final JavaMailSender mailSender;
    private final EmailConfiguration emailConfiguration;
    private GeneralUser theUser;
    @Override
    public void onApplicationEvent(ResetPasswordViaEmailEvent event) {
        theUser = event.getUser();
        String verificationToken = UUID.randomUUID().toString();
        userService.updateUserVerificationToken(theUser, verificationToken);
        String url = event.getApplicationUrl()+"/verify-email?token="+verificationToken;
        try {
            sendResetPasswordEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendResetPasswordEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Reset Password";
        String senderName = "Service Lagbe";
        String mailContent = "<p> Hi, "+ theUser.getFirstName()+ ", </p>"+
                "<p>Please, follow the link below to reset your password.</p>"+
                "<a href=\"" +url+ "\">Reset Password</a>"+
                "<p> Thank you <br> Service Lagbe";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom(emailConfiguration.getUsername(), senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }
}
