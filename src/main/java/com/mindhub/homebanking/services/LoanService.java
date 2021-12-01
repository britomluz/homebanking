package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.models.Movement;

import java.util.List;
import java.util.Optional;

public interface LoanService {
    public List<Loan> getAll();
    public Optional<Loan> getById(Long id);
    public Loan save(Loan loan);
    public Loan getByName(String name);
}
