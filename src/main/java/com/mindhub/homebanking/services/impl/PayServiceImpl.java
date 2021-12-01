package com.mindhub.homebanking.services.impl;

import com.mindhub.homebanking.models.Pay;
import com.mindhub.homebanking.repositories.PayRepository;
import com.mindhub.homebanking.services.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PayServiceImpl implements PayService {

    @Autowired
    PayRepository payRepository;

    @Override
    public List<Pay> getAll() {
        return payRepository.findAll();
    }

    @Override
    public Optional<Pay> getById(Long id) {
        return payRepository.findById(id);
    }

    @Override
    public Pay save(Pay pay) {
        return payRepository.save(pay);
    }

    @Override
    public void delete(Pay pay) {
        payRepository.delete(pay);
    }
}
