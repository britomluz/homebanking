package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Contact;
import com.mindhub.homebanking.models.ContactType;

public class ContactDTO {
    private Long id;
    private String nameContact;
    private String accountContact;
    private ContactType type;

    public ContactDTO() { }

    public ContactDTO(Contact contact) {
        this.id = contact.getId();
        this.nameContact = contact.getNameContact();
        this.accountContact = contact.getAccountContact();
        this.type = contact.getType();
    }

    public Long getId() {
        return id;
    }

    public String getNameContact() {
        return nameContact;
    }

    public void setNameContact(String nameContact) {
        this.nameContact = nameContact;
    }

    public String getAccountContact() {
        return accountContact;
    }

    public void setAccountContact(String accountContact) {
        this.accountContact = accountContact;
    }

    public ContactType getType() { return type; }

    public void setType(ContactType type) { this.type = type; }
}
