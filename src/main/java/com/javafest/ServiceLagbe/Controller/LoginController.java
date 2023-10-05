package com.javafest.ServiceLagbe.Controller;

import com.javafest.ServiceLagbe.event.OTPRegistrationCompleteEvent;
import com.javafest.ServiceLagbe.event.ResetPasswordViaEmailEvent;
import com.javafest.ServiceLagbe.records.OTPRequest;
import com.javafest.ServiceLagbe.records.RequestEmailOrNumber;
import com.javafest.ServiceLagbe.records.RequestNewPassword;
import com.javafest.ServiceLagbe.users.general.GeneralUser;
import com.javafest.ServiceLagbe.users.general.GeneralUserRepository;
import com.javafest.ServiceLagbe.users.general.GeneralUserService;
import com.javafest.ServiceLagbe.users.worker.WorkerUser;
import com.javafest.ServiceLagbe.users.worker.WorkerUserRepository;
import com.javafest.ServiceLagbe.users.worker.WorkerUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final GeneralUserService generalUserService;
    private final WorkerUserService workerUserService;
    private final GeneralUserRepository generalUserRepository;
    private final WorkerUserRepository workerUserRepository;
    private final ApplicationEventPublisher publisher;
    private GeneralUser generalUser;
    private WorkerUser workerUser;
    private String message;
    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("message", message);
        message = null;
        return "login-page";
    }
    @GetMapping("/forget-password")
    public String goToForgetPasswordPage(@ModelAttribute("request") RequestEmailOrNumber request) {
        return "forget-password";
    }
    @PostMapping("/forget-password")
    public String sendUsername(@ModelAttribute("verificationOTP") OTPRequest otpRequest, @ModelAttribute("request") RequestEmailOrNumber request, final HttpServletRequest httpRequest, Model model) {
        if (request.username().contains("@")) {
            Optional<GeneralUser> user = generalUserRepository.findByEmail(request.username());
            if (user.isEmpty()) {
                model.addAttribute("message", "Email does not exists.");
                return "forget-password";
            }
            generalUser = user.get();
            publisher.publishEvent(new ResetPasswordViaEmailEvent(generalUser, applicationUrl(httpRequest)));
            model.addAttribute("message", "Please check your email to verify.");
            return "forget-password";
        } else {
            Optional<WorkerUser> user = workerUserRepository.findByNumber(request.username());
            if (user.isEmpty()) {
                model.addAttribute("message", "Number does not exists.");
                return "forget-password";
            }
            workerUser = user.get();
            publisher.publishEvent(new OTPRegistrationCompleteEvent(workerUser));
            return "otp-verification";
        }
    }
    @GetMapping("/verify-email")
    public String goToResetPassword(@ModelAttribute("request") RequestNewPassword request, @RequestParam("token") String token, Model model) {
        model.addAttribute("message", "Email verified successfully. Now you can reset your password.");
        return "reset-password";
    }
    @PostMapping("/verify-otp")
    public String goToResetPin(@ModelAttribute("verificationOTP") OTPRequest otpRequest, @ModelAttribute("request") RequestNewPassword request, Model model) {
        String otp = otpRequest.d1()+otpRequest.d2()+otpRequest.d3()+otpRequest.d4()+otpRequest.d5()+otpRequest.d6();
        String verificationResult = workerUserService.validateOTP(otp);
        if (verificationResult.equalsIgnoreCase("valid")){
            model.addAttribute("message", "Number verified successfully. Now you can reset your password.");
            return "reset-password";
        }
        model.addAttribute("message", verificationResult);
        return "otp-verification";
    }
    @PostMapping("/reset-password")
    public String resetPassword(@ModelAttribute("request") RequestNewPassword request) {
        if (generalUser!=null) {
            generalUserService.resetPassword(request.password(), generalUser);
            generalUser = null;
        }
        else if (workerUser!=null) {
            workerUserService.resetPassword(request.password(), workerUser);
            workerUser = null;
        }
        message = "Password Reset successful. Please login";
        return "redirect:login";
    }
    public String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }

}
