package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface MovementRepository extends JpaRepository<Movement, Long> {
}
