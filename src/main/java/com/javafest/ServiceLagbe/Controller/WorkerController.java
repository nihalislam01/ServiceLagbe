package com.javafest.ServiceLagbe.Controller;

import com.javafest.ServiceLagbe.notification.Notification;
import com.javafest.ServiceLagbe.notification.NotificationService;
import com.javafest.ServiceLagbe.projects.Project;
import com.javafest.ServiceLagbe.projects.ProjectService;
import com.javafest.ServiceLagbe.records.OTPRequest;
import com.javafest.ServiceLagbe.security.CustomUserDetailsService;
import com.javafest.ServiceLagbe.users.worker.WorkerUser;
import com.javafest.ServiceLagbe.users.worker.WorkerUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/worker")
public class WorkerController {

    private final CustomUserDetailsService userDetailsService;
    private final WorkerUserService workerUserService;
    private final ProjectService projectService;
    private final NotificationService notificationService;
    @GetMapping("/dashboard")
    public String showWorkerHome(@Param("keyword") String keyword, Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        WorkerUser user = workerUserService.findByNumber(userDetails.getUsername());
        List<Project> projects = projectService.getProjects(keyword);
        List<Project> reversedList = projects.stream()
                .sorted((a, b) -> -1)
                .collect(Collectors.toList());
        model.addAttribute("user", user);
        model.addAttribute("projects", reversedList);
        return "worker-dashboard";
    }

    @GetMapping("/profile")
    public String showWorkerProfile(Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        WorkerUser user = workerUserService.findByNumber(userDetails.getUsername());
        model.addAttribute("user", user);
        boolean availability = user.getIsAvailable().equalsIgnoreCase("Yes");
        model.addAttribute("availability", availability);
        return "worker-profile";
    }
    @GetMapping("verify-otp")
    public String goToVerifyOTP(@ModelAttribute("verificationOTP") OTPRequest otpRequest) {
        
        return "otp-verification";
    }
    @GetMapping("/change-availability")
    public String updateWorkerProfile(Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        WorkerUser user = workerUserService.findByNumber(userDetails.getUsername());
        model.addAttribute("user", user);
        boolean availability = user.getIsAvailable().equalsIgnoreCase("Yes");
        if (availability) {
            workerUserService.setIsAvailable("No", user);
        } else {
            workerUserService.setIsAvailable("Yes", user);
        }
        model.addAttribute("availability", !availability);
        return "redirect:profile";
    }
    @GetMapping("/apply")
    public String applyForWork(@RequestParam Long id, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        WorkerUser user = workerUserService.findByNumber(userDetails.getUsername());
        Project project = projectService.findById(id);
        notificationService.createGeneralNotification(project, user);
        return "redirect:dashboard";
    }
    @GetMapping("/notification")
    public String goToNotification(Model model, Principal principal){
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        Predicate<? super Notification> predicate = notification -> notification.getNumber().equalsIgnoreCase(userDetails.getUsername());
        List<Notification> notifications = notificationService.getNotifications().stream().filter(predicate).toList();
        List<Notification> reversedList = notifications.stream()
                .sorted((a, b) -> -1)
                .collect(Collectors.toList());
        model.addAttribute("notifications", reversedList);
        return "worker-notification";
    }
}
