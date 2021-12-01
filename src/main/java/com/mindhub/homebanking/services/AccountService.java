package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    public List<Account> getAll();
    public Optional<Account> getById(Long id);
    public Account getByNumber(String number);
    public Account save(Account account);
    public List<Account> listAll();
    public void deleteAccount(Account account);
}
