package com.javafest.ServiceLagbe.Controller;

import com.javafest.ServiceLagbe.notification.Notification;
import com.javafest.ServiceLagbe.notification.NotificationService;
import com.javafest.ServiceLagbe.posts.PostService;
import com.javafest.ServiceLagbe.projects.Project;
import com.javafest.ServiceLagbe.projects.ProjectService;
import com.javafest.ServiceLagbe.records.CreateProjectRequest;
import com.javafest.ServiceLagbe.security.CustomUserDetailsService;
import com.javafest.ServiceLagbe.users.general.GeneralUser;
import com.javafest.ServiceLagbe.users.general.GeneralUserService;
import com.javafest.ServiceLagbe.users.worker.WorkerUser;
import com.javafest.ServiceLagbe.users.worker.WorkerUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/general")
public class GeneralController {

    private final CustomUserDetailsService userDetailsService;
    private final GeneralUserService generalUserService;
    private final WorkerUserService workerUserService;
    private final ProjectService projectService;
    private final NotificationService notificationService;
    private final PostService postService = new PostService();
    private String workerNumber;
    private List<String> posts = postService.getPosts();
    private Project projectProfile;
    @GetMapping("/dashboard")
    public String showGeneralDashboard(@ModelAttribute("project")CreateProjectRequest projectRequest, @Param("keyword") String keyword, Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        GeneralUser user = generalUserService.findByEmail(userDetails.getUsername());
        Predicate<? super Project> predicate = project-> project.getEmail().equalsIgnoreCase(userDetails.getUsername());
        List<Project> projects = projectService.findAll().stream().filter(predicate).toList();
        List<Project> reversedProjectList = projects.stream()
                .sorted((a, b) -> -1)
                .collect(Collectors.toList());
        List<WorkerUser> workers = generalUserService.getWorkers(keyword);
        List<WorkerUser> reversedWorkerList = workers.stream()
                .sorted((a, b) -> -1)
                .collect(Collectors.toList());
        model.addAttribute("workers", reversedWorkerList);
        model.addAttribute("posts", posts);
        model.addAttribute("user", user);
        model.addAttribute("projects", reversedProjectList);
        return "general-dashboard";
    }
    @GetMapping("/worker")
    public String goToWorkersPage(@ModelAttribute("project")CreateProjectRequest projectRequest, @Param("keyword") String keyword, Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        Predicate<? super Project> predicate = project-> project.getEmail().equalsIgnoreCase(userDetails.getUsername());
        List<Project> projects = projectService.findAll().stream().filter(predicate).toList();
        List<WorkerUser> workers = generalUserService.getWorkers(keyword);
        List<WorkerUser> reversedList = workers.stream()
                .sorted((a, b) -> -1)
                .collect(Collectors.toList());
        model.addAttribute("workers", reversedList);
        model.addAttribute("posts", posts);
        model.addAttribute("projects", projects);
        return "workers";
    }
    @PostMapping("/add-project")
    public String addProject(@ModelAttribute("project")CreateProjectRequest projectRequest, Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        GeneralUser user = generalUserService.findByEmail(userDetails.getUsername());
        projectService.createProject(projectRequest, user);
        model.addAttribute("message", "Project Added Successfully.");
        return "redirect:dashboard";
    }
    @PostMapping("/delete-project")
    public String deleteProject(@RequestParam Long id) {
        projectService.deleteById(id);
        return "redirect:dashboard";
    }
    @GetMapping("/project-profile")
    public String projectProfile(@ModelAttribute("project")CreateProjectRequest projectRequest,@Param("keyword") String keyword, Model model) {
        List<WorkerUser> workers = generalUserService.getWorkers(keyword);
        List<WorkerUser> reversedWorkerList = workers.stream()
                .sorted((a, b) -> -1)
                .collect(Collectors.toList());
        model.addAttribute("workers", reversedWorkerList);
        model.addAttribute("posts", posts);
        model.addAttribute("project", projectProfile);
        return "general-project-profile";
    }
    @PostMapping("/project-profile")
    public String goToProjectProfile(@ModelAttribute("project")CreateProjectRequest projectRequest,@Param("keyword") String keyword, @RequestParam Long id , Model model) {
        projectProfile = projectService.findById(id);
        List<WorkerUser> workers = generalUserService.getWorkers(keyword);
        List<WorkerUser> reversedWorkerList = workers.stream()
                .sorted((a, b) -> -1)
                .collect(Collectors.toList());
        model.addAttribute("workers", reversedWorkerList);
        model.addAttribute("posts", posts);
        model.addAttribute("project", projectProfile);
        return "general-project-profile";
    }
    @GetMapping("/add-worker")
    public String addWorkerToProject(@RequestParam Long id , @RequestParam String number, Principal principal, Model model) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        GeneralUser user = generalUserService.findByEmail(userDetails.getUsername());
        model.addAttribute("user", user);
        WorkerUser worker = workerUserService.findByNumber(number);
        Project project = projectService.findById(id);
        projectService.updateProject(project, worker);
        notificationService.createAddToProjectNotification(project, worker);
        return "redirect:worker";
    }
    @PostMapping("/rate-worker")
    public String rateWorkerFromProject(@ModelAttribute("project")CreateProjectRequest projectRequest, @RequestParam Long rating, @RequestParam String hiddenNumber,@RequestParam Long id,  Model model) {
        WorkerUser worker = workerUserService.findByNumber(hiddenNumber);
        workerUserService.rateWorker(rating, worker);
        projectProfile = projectService.findById(id);
        return "redirect:project-profile";
    }
    @PostMapping("/remove-worker")
    public String removeWorkerFromProject(@ModelAttribute("project")CreateProjectRequest projectRequest, @RequestParam String number) {
        WorkerUser worker = workerUserService.findByNumber(number);
        projectService.removeWorker(projectProfile, worker);
        notificationService.createRemoveFromProjectNotification(projectProfile, worker);
        return "redirect:project-profile";
    }
    @GetMapping("/profile")
    public String showGeneralProfile(@ModelAttribute("project")CreateProjectRequest projectRequest,@Param("keyword") String keyword, Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        GeneralUser user = generalUserService.findByEmail(userDetails.getUsername());
        List<WorkerUser> workers = generalUserService.getWorkers(keyword);
        List<WorkerUser> reversedWorkerList = workers.stream()
                .sorted((a, b) -> -1)
                .collect(Collectors.toList());
        model.addAttribute("workers", reversedWorkerList);
        model.addAttribute("posts", posts);
        model.addAttribute("user", user);
        return "general-profile";
    }
    @GetMapping("/notification")
    public String goToNotification(@ModelAttribute("project")CreateProjectRequest projectRequest, @Param("keyword") String keyword, Principal principal, Model model) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        GeneralUser user = generalUserService.findByEmail(userDetails.getUsername());
        Predicate<? super Notification> predicate = notification -> notification.getEmail().equalsIgnoreCase(userDetails.getUsername());
        List<Notification> notifications = notificationService.getNotifications().stream().filter(predicate).toList();
        List<Notification> reversedList = notifications.stream()
                .sorted((a, b) -> -1)
                .collect(Collectors.toList());
        List<WorkerUser> workers = generalUserService.getWorkers(keyword);
        List<WorkerUser> reversedWorkerList = workers.stream()
                .sorted((a, b) -> -1)
                .collect(Collectors.toList());
        model.addAttribute("workers", reversedWorkerList);
        model.addAttribute("posts", posts);
        model.addAttribute("notifications", reversedList);
        model.addAttribute("user", user);
        return "general-notification";
    }
}
