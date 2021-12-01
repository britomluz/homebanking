package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientType;
import com.mindhub.homebanking.models.Contact;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String url;
    private ClientType role;
    private Set<AccountDTO> accounts = new HashSet<>();
    private Set<ClientLoanDTO> loans = new HashSet<>();
    private Set<CardDTO> cards = new HashSet<>();
    private Set<MovementDTO> movements = new HashSet<>();
    private Set<ContactDTO> contacts = new HashSet<>();

    public ClientDTO(Client client) {
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.role = client.getRole();
        this.url = client.getUrl();
        this.accounts = client.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toSet());
        this.loans = client.getClientLoans().stream().map(ClientLoanDTO::new).collect(Collectors.toSet());
        this.cards = client.getCards().stream().map(CardDTO::new).collect(Collectors.toSet());
        this.movements = client.getMovements().stream().map(MovementDTO::new).collect(Collectors.toSet());
        this.contacts = client.getContacts().stream().map(ContactDTO::new).collect(Collectors.toSet());

    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ClientType getRole() {
        return role;
    }

    public void setRole(ClientType role) {
        this.role = role;
    }

    public Set<AccountDTO> getAccounts() {
        return accounts;
    }
    public Set<ClientLoanDTO> getLoans() {
        return loans;
    }
    public Set<CardDTO> getCards(){return cards; }
    public Set<MovementDTO> getMovements() {
        return movements;
    }
    public Set<ContactDTO> getContacts(){return contacts; }


}
