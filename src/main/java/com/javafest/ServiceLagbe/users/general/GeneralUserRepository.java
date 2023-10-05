package com.javafest.ServiceLagbe.users.general;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GeneralUserRepository extends JpaRepository<GeneralUser, Long> {
    public Optional<GeneralUser> findByEmail(String email);
}
