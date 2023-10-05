package com.javafest.ServiceLagbe.users.worker;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WorkerUserRepository extends JpaRepository<WorkerUser, Long> {
    Optional<WorkerUser> findByNumber(String number);
    @Query("SELECT u FROM WorkerUser u WHERE CONCAT(u.firstName, u.lastName, u.location, u.rating) LIKE %?1%")
    public List<WorkerUser> findAll(String keyword);
}
