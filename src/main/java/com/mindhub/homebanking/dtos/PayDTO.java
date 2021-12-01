package com.mindhub.homebanking.dtos;
import com.mindhub.homebanking.models.Pay;
import java.time.LocalDateTime;

public class PayDTO {
    private Long id;
    private LocalDateTime date;
    private String description;
    private double amount;
    private int payments;
    private double amountPayments;


    public PayDTO(Pay pay) {
        this.id = pay.getId();
        this.date = pay.getDate();
        this.description = pay.getDescription();
        this.amount = pay.getAmount();
        this.payments = pay.getPayments();
        this.amountPayments = pay.getAmountPayments();
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
}
