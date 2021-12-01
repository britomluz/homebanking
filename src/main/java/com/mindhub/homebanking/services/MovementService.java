package com.mindhub.homebanking.services;


import com.mindhub.homebanking.models.Movement;


import java.util.List;
import java.util.Optional;

public interface MovementService {
    public List<Movement> getAll();
    public Optional<Movement> getById(Long id);
    public Movement save(Movement movement);
}
