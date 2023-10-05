package com.javafest.ServiceLagbe.projects;

import com.javafest.ServiceLagbe.users.worker.WorkerUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;

    @OneToMany(fetch = FetchType.EAGER)
    private List<WorkerUser> workers;
    private String location;
    private List<String> lookingFor;
}
