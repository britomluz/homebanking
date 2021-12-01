package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String nameContact;
    private String accountContact;
    private ContactType type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    public Contact() {    }

    public Contact(String nameContact, String accountContact, ContactType type, Client client) {
        this.nameContact = nameContact;
        this.accountContact = accountContact;
        this.client = client;
        this.type = type;

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

    public ContactType getType() {
        return type;
    }

    public void setType(ContactType type) {
        this.type = type;
    }

    @JsonIgnore
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
