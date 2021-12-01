package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Contact;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface ContactService {

    public List<Contact> getAll();
    public Optional<Contact> getById(Long id);
    public Contact save(Contact contact);
    public Contact getByAccountContact(String number);
}
