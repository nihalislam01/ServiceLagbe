package com.javafest.ServiceLagbe.notification;

import com.javafest.ServiceLagbe.users.worker.WorkerUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.SimpleTimeZone;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String number;
    private String description;
}
