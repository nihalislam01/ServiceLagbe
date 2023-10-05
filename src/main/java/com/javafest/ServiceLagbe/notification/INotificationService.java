package com.javafest.ServiceLagbe.notification;

import com.javafest.ServiceLagbe.projects.Project;
import com.javafest.ServiceLagbe.users.worker.WorkerUser;

public interface INotificationService {
    public void createGeneralNotification(Project project, WorkerUser user);
    public void createAddToProjectNotification(Project project, WorkerUser workerUser);
    public void createRemoveFromProjectNotification(Project project, WorkerUser workerUser);
}
