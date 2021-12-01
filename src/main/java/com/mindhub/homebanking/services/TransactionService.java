package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Transaction;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface TransactionService {
    public Transaction save(Transaction transaction);
    public Optional<Transaction> getById(Long id);
    public List<Transaction> getAllByDateBetween(LocalDateTime from, LocalDateTime to);
    public void deleteAllTransactions(Set<Transaction> transactions);
}
