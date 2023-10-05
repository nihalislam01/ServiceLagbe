package com.javafest.ServiceLagbe.projects;

import com.javafest.ServiceLagbe.records.CreateProjectRequest;
import com.javafest.ServiceLagbe.users.general.GeneralUser;
import com.javafest.ServiceLagbe.users.worker.WorkerUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class ProjectService implements IProjectService {

    private final ProjectRepository projectRepository;

    @Override
    public Project createProject(CreateProjectRequest request, GeneralUser user) {
        var newProject = new Project();
        newProject.setName(request.name());
        newProject.setLocation(request.location());
        newProject.setLookingFor(request.lookingFor());
        newProject.setWorkers(new ArrayList<>());
        newProject.setEmail(user.getEmail());
        projectRepository.save(newProject);
        return newProject;
    }

    @Override
    public List<Project> getProjects(String keyword) {
        if (keyword!=null) {
            List<Project> projects = projectRepository.findAll(keyword);
            return projects;
        }
        List<Project> projects = projectRepository.findAll();
        return projects;
    }

    @Override
    public void deleteById(Long id) {
        projectRepository.deleteById(id);
    }

    @Override
    public void updateProject(Project project, WorkerUser worker) {
        Predicate<? super WorkerUser> predicate = user -> user.getNumber().equalsIgnoreCase(worker.getNumber());
        Optional<WorkerUser> findAny = project.getWorkers().stream().filter(predicate).findAny();
        if (findAny.isEmpty()) {
            project.getWorkers().add(worker);
            projectRepository.save(project);
        }
    }
    @Override
    public void removeWorker(Project project, WorkerUser worker) {
        Predicate<? super WorkerUser> predicate = user -> user.getNumber().equalsIgnoreCase(worker.getNumber());
        project.getWorkers().removeIf(predicate);
        projectRepository.save(project);
    }

    @Override
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Project findById(Long id) {
        Optional<Project> project = projectRepository.findById(id);
        return project.get();
    }
}
