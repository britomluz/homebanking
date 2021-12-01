package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Entity
@Validated
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String url;
    private ClientType role;

    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    Set<Account> accounts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    Set<ClientLoan>  clientLoans= new LinkedHashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    Set<Card> cards = new LinkedHashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    Set<Movement> movements = new LinkedHashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    Set<Contact> contacts = new LinkedHashSet<>();

    public Client() { }

    public Client(String firstName, String lastName, String email, String password, String url, ClientType role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.url = url;
        this.role = role;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public void addAccount(Account account) {
        account.setClient(this);
        accounts.add(account);
    }

    public Long getId() {    return id;   }

    public String getFirstName() { return firstName;}
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getUrl() {  return url; }
    public void setUrl(String url) {  this.url = url; }

    public ClientType getRole() { return role; }
    public void setRole(ClientType role) { this.role = role; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password;  }

    public void setPassword(String password) {    this.password = password;   }

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public void setClientLoans(Set<ClientLoan> clientLoans) {
        this.clientLoans = clientLoans;
    }



    public void addClientLoan(ClientLoan clientLoan){
        clientLoan.setClient(this);
        clientLoans.add(clientLoan);
    }

    @JsonIgnore
    public List<Loan> getLoans(){
        return clientLoans.stream().map(ClientLoan::getLoan).collect(Collectors.toList());
    }

    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }
    public void addCard(Card card){
        card.setClient(this);
        cards.add(card);
    }

    public Set<Movement> getMovements() { return movements; }
    public void setMovements(Set<Movement> movements) { this.movements = movements; }

    public void addMovement(Movement movement){
        movement.setClient(this);
        movements.add(movement);
    }

    public Set<Contact> getContacts() { return contacts;     }

    public void setContacts(Set<Contact> contacts) { this.contacts = contacts; }

    public void addContact(Contact person){
        person.setClient(this);
        contacts.add(person);
    }



    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

