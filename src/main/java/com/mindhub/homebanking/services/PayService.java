package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Pay;

import java.util.List;
import java.util.Optional;

public interface PayService {
    public List<Pay> getAll();
    public Optional<Pay> getById(Long id);
    public Pay save(Pay pay);
    public void delete(Pay pay);
}
