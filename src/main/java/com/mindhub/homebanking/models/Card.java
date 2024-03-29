package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class Card {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private CardType type;
    private CardColor color;
    private String cardholder;
    private String number;
    private int cvv;
    private LocalDateTime fromDate;
    private LocalDateTime thruDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="account_id")
    Account account;

    @OneToMany(mappedBy="card", fetch=FetchType.EAGER)
    Set<Pay> pays = new LinkedHashSet<>();

    public Card() { }

    public Card(CardType type, CardColor color, String cardholder, String number, int cvv, LocalDateTime fromDate, LocalDateTime thruDate, Client client) {
        this.type = type;
        this.color = color;
        this.cardholder = cardholder;
        this.number = number;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
        this.client = client;
    }


    public Set<Pay> getPays() {    return pays; }

    public void setPays(Set<Pay> pays) { this.pays = pays; }

    public void addPay(Pay pay) {
        pay.setCard(this);
        pays.add(pay);
    }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }

    public void addAccount(Account account) {
        account.setCard(this);
        //   card.add(card);
    }


    public Long getId() {
        return id;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public CardColor getColor() {
        return color;
    }

    public void setColor(CardColor color) {
        this.color = color;
    }

    public String getCardholder() { return cardholder; }

    public void setCardholder(String cardholder) {
        this.cardholder = cardholder;
    }

    public String getNumber() { return number;}

    public void setNumber(String number) {
        this.number = number;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public LocalDateTime getThruDate() { return thruDate;}

    public void setThruDate(LocalDateTime thruDate) {
        this.thruDate = thruDate;
    }

    public LocalDateTime getFromDate() { return fromDate; }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }
    @JsonIgnore
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }







}
