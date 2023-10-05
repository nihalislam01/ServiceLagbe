package com.javafest.ServiceLagbe.users.worker;

import com.javafest.ServiceLagbe.projects.Project;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class WorkerUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String rating;
    private long totalRating;
    private long personRated;
    private String firstName;
    private String lastName;
    @NaturalId(mutable = true)
    private String number;
    private String pin;
    private String location;
    private String role;
    private String isAvailable;
    private List<String> posts;
    private boolean isEnable;
}
