package com.javafest.ServiceLagbe.Controller;

import com.javafest.ServiceLagbe.event.OTPRegistrationCompleteEvent;
import com.javafest.ServiceLagbe.event.EmailRegistrationCompleteEvent;
import com.javafest.ServiceLagbe.posts.PostService;
import com.javafest.ServiceLagbe.records.GeneralRegistrationRequest;
import com.javafest.ServiceLagbe.records.OTPRequest;
import com.javafest.ServiceLagbe.records.WorkerRegistrationRequest;
import com.javafest.ServiceLagbe.users.general.GeneralUser;
import com.javafest.ServiceLagbe.users.general.GeneralUserService;
import com.javafest.ServiceLagbe.users.worker.WorkerUser;
import com.javafest.ServiceLagbe.users.worker.WorkerUserService;
import com.javafest.ServiceLagbe.verification.VerificationOTP;
import com.javafest.ServiceLagbe.verification.VerificationOTPRepository;
import com.javafest.ServiceLagbe.verification.VerificationToken;
import com.javafest.ServiceLagbe.verification.VerificationTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegistrationController {

    private final GeneralUserService generalUserService;
    private final WorkerUserService workerUserService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenRepository tokenRepository;
    private final VerificationOTPRepository otpRepository;
    private final PostService postService;
    private String message;

    @GetMapping("/worker")
    public String showWorkerRegistrationPage(@ModelAttribute("user") WorkerRegistrationRequest workerRegistrationRequest, Model model) {
        List<String> posts = postService.getPosts();
        model.addAttribute("posts", posts);
        model.addAttribute("message", message);
        message = null;
        return "worker-register";
    }
    @PostMapping("/worker")
    public String registerWorker(@ModelAttribute("user") WorkerRegistrationRequest workerRegistrationRequest, @ModelAttribute("verificationOTP") OTPRequest otpRequest, Model model){
        WorkerUser user = workerUserService.register(workerRegistrationRequest);
        if (user.getNumber()==null) {
            model.addAttribute("message", "Number already exists.");
            return "worker-register";
        }
        publisher.publishEvent(new OTPRegistrationCompleteEvent(user));
        return "otp-verification";
    }

    @GetMapping("/general")
    public String showGeneralRegistrationPage(@ModelAttribute("user") GeneralRegistrationRequest generalRegistrationRequest, Model model) {
        model.addAttribute("message", message);
        message = null;
        return "general-register";
    }
    @PostMapping("/general")
    public String registerGeneral(@ModelAttribute("user") GeneralRegistrationRequest generalRegistrationRequest, final HttpServletRequest request, Model model){
        GeneralUser user = generalUserService.register(generalRegistrationRequest);
        if (user.getEmail()==null) {
            model.addAttribute("message", "Email already exists.");
            return "general-register";
        }
        publisher.publishEvent(new EmailRegistrationCompleteEvent(user, applicationUrl(request)));
        model.addAttribute("message", "Registered Successfully! Please Check your email to verify.");
        return "general-register";
    }
    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token){
        VerificationToken theToken = tokenRepository.findByToken(token);
        if (theToken.getUser().isEnable()){
            message = "This account has already been verified, please, go to login page to login.";
            return "redirect:general";
        }
        String verificationResult = generalUserService.validateToken(token);
        if (verificationResult.equalsIgnoreCase("valid")){
            message = "Email verified successfully. Now you can go to login page to login.";
            return "redirect:general";
        } else {
            message = "Invalid verification token";
            return "redirect:general";
        }
    }
    @GetMapping("verify-otp")
    public String goToVerifyOTP(@ModelAttribute("verificationOTP") OTPRequest otpRequest) {
        return "otp-verification";
    }

    @PostMapping("/verify-otp")
    public String verifyOTP(@ModelAttribute("verificationOTP") OTPRequest otpRequest, Model model){
        String otp = otpRequest.d1()+otpRequest.d2()+otpRequest.d3()+otpRequest.d4()+otpRequest.d5()+otpRequest.d6();
        VerificationOTP verificationOTP = otpRepository.findByOtp(otp);
        if (verificationOTP==null) {
            model.addAttribute("message", "Invalid verification OTP");
            return "otp-verification";
        }
        if (verificationOTP.getUser().isEnable()){
            message="This account has already been verified, please, go to login page to login.";
            return "redirect:worker";
        }
        String verificationResult = workerUserService.validateOTP(otp);
        if (verificationResult.equalsIgnoreCase("valid")){
            message = "Number verified successfully. Now you can go to login page to login.";
            return "redirect:worker";
        }
        model.addAttribute("message", "Invalid verification OTP");
        return "otp-verification";
    }

    public String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }

}
