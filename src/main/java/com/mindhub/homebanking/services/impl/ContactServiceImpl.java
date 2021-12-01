package com.mindhub.homebanking.services.impl;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Contact;
import com.mindhub.homebanking.repositories.ContactRepository;
import com.mindhub.homebanking.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;


    @Override
    public List<Contact> getAll() {
        return contactRepository.findAll();
    }

    @Override
    public Optional<Contact> getById(Long id){
        return contactRepository.findById(id);
    }

    @Override
    public Contact save(Contact contact) {
        return contactRepository.save(contact);
    }

    @Override
    public Contact getByAccountContact(String number) {
        return contactRepository.findByAccountContact(number);
    }

}
