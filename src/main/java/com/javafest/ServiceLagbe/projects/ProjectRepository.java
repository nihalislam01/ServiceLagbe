package com.javafest.ServiceLagbe.projects;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    public Optional<Project> findById(Long id);

    @Query("SELECT p FROM Project p WHERE CONCAT(p.name, p.location) LIKE %?1%")
    public List<Project> findAll(String keyword);
//            " AND SELECT w FROM Project.lookingFor w WHERE w LIKE %?1%")
}
