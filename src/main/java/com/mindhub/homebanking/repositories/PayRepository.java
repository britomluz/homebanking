package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Pay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PayRepository extends JpaRepository<Pay, Long> {
}
