package com.mindhub.homebanking.services.impl;

import com.mindhub.homebanking.models.Movement;
import com.mindhub.homebanking.repositories.MovementRepository;
import com.mindhub.homebanking.services.MovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovementServiceImpl implements MovementService {

    @Autowired
    MovementRepository movementRepository;

    @Override
    public List<Movement> getAll() {
        return null;
    }

    @Override
    public Optional<Movement> getById(Long id) {
        return Optional.empty();
    }

    @Override
    public Movement save(Movement movement) {
        return movementRepository.save(movement);
    }
}
