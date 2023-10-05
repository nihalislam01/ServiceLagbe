package com.javafest.ServiceLagbe.notification;

import com.javafest.ServiceLagbe.projects.Project;
import com.javafest.ServiceLagbe.users.general.GeneralUser;
import com.javafest.ServiceLagbe.users.general.GeneralUserService;
import com.javafest.ServiceLagbe.users.worker.WorkerUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService implements INotificationService {

    private final NotificationRepository notificationRepository;
    private final GeneralUserService generalUserService;
    @Override
    public void createGeneralNotification(Project project, WorkerUser user) {
        Notification newNotification = new Notification();
        newNotification.setEmail(project.getEmail());
        newNotification.setNumber("");
        String description = user.getFirstName() + " " + user.getLastName() +" with rating "+ user.getRating() + "(" + user.getPersonRated() + ")" + " wants to apply for your " + project.getName() +". Contact " + user.getFirstName() + " by " + user.getNumber() + ".";
        newNotification.setDescription(description);
        notificationRepository.save(newNotification);
    }
    @Override
    public void createAddToProjectNotification(Project project, WorkerUser workerUser) {
        GeneralUser generalUser = generalUserService.findByEmail(project.getEmail());
        Notification newNotification = new Notification();
        newNotification.setEmail("");
        newNotification.setNumber(workerUser.getNumber());
        String description = generalUser.getFirstName() + " " + generalUser.getLastName() + " added you to their " + project.getName() + " project";
        newNotification.setDescription(description);
        notificationRepository.save(newNotification);
    }

    @Override
    public void createRemoveFromProjectNotification(Project project, WorkerUser workerUser) {
        GeneralUser generalUser = generalUserService.findByEmail(project.getEmail());
        Notification newNotification = new Notification();
        newNotification.setEmail("");
        newNotification.setNumber(workerUser.getNumber());
        String description = generalUser.getFirstName() + " " + generalUser.getLastName() + " removed you from their " + project.getName() + " project";
        newNotification.setDescription(description);
        notificationRepository.save(newNotification);
    }
    public List<Notification> getNotifications() {
        return notificationRepository.findAll();
    }
 }
