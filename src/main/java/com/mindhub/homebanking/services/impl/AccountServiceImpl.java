package com.mindhub.homebanking.services.impl;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<Account> getAll(){
        return accountRepository.findAll();
    }

    @Override
    public Optional<Account> getById(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    public Account getByNumber(String number) {
        return accountRepository.findByNumber(number);
    }

    @Override
    public Account save(Account account) {
        return accountRepository.save(account);
    }

   // public List<Account> listAll(){
    //    return accountRepository.findAll(Sort.by("id").ascending());
   // }

    public List<Account> listAll() {
        return accountRepository.findAll(Sort.by("id").ascending());
    }

    @Override
    public void deleteAccount(Account account) {
        accountRepository.delete(account);
    }
}
