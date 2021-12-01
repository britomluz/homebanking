package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String number;
    private LocalDateTime creationDate;
    private AccountType type;
    private double balance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    @OneToMany(mappedBy="account", fetch=FetchType.EAGER)
    Set<Transaction> transactions = new LinkedHashSet<>();

    @OneToOne(mappedBy = "account", fetch = FetchType.EAGER)
    @JoinColumn(name="card_id")
    Card card;

    public Account() {    }

    public Account(String number, LocalDateTime creationDate, AccountType type, double balance, Client client) {
        this.number = number;
        this.creationDate = creationDate;
        this.type = type;
        this.balance = balance;
        this.client = client;
    }

    public Set<Transaction> getTransactions() { return transactions; }

    public void setTransactions(Set<Transaction> transactions) { this.transactions = transactions; }

    public void addTransaction(Transaction transaction) {
        transaction.setAccount(this);
        transactions.add(transaction);
    }

    public Card getCard() { return card; }

    public void setCard(Card card) {
        this.card = card;
    }

    public void addCard(Card cardA) {
        cardA.setAccount(this);
     //   card.add(card);
    }

    public Long getId() { return id; }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public AccountType getType() { return type; }

    public void setType(AccountType type) { this.type = type; }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @JsonIgnore
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }


}
