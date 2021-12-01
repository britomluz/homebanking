package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Pay {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private LocalDateTime date;
    private String description;
    private double amount;
    private int payments;
    private double amountPayments;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="card_id")
    private Card card;

    public Pay() {
    }

    public Pay(LocalDateTime date, String description, double amount, int payments, double amountPayments, Card card) {
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.payments = payments;
        this.amountPayments = amountPayments;
        this.card = card;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getPayments() {
        return payments;
    }

    public void setPayments(int payments) {
        this.payments = payments;
    }

    public double getAmountPayments() {
        return amountPayments;
    }

    public void setAmountPayments(double amountPayments) {
        this.amountPayments = amountPayments;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
