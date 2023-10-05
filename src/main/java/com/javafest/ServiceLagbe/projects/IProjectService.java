package com.javafest.ServiceLagbe.projects;

import com.javafest.ServiceLagbe.records.CreateProjectRequest;
import com.javafest.ServiceLagbe.users.general.GeneralUser;
import com.javafest.ServiceLagbe.users.worker.WorkerUser;

import java.util.List;
import java.util.Optional;

public interface IProjectService {
    public Project createProject(CreateProjectRequest request, GeneralUser user);
    public List<Project> getProjects(String keyword);
    public void deleteById(Long id);
    public void updateProject(Project project, WorkerUser worker);
    public List<Project> findAll();
    public Project findById(Long id);
    public void removeWorker(Project project, WorkerUser worker);
}
