package com.mindhub.homebanking.dtos;

import java.util.ArrayList;
import java.util.List;

public class LoanApplicationDTO {
    private String id;
    private String amount;
    private String payments;
    private String accountTo;

    public LoanApplicationDTO() { }

    public LoanApplicationDTO(String id, String amount, String payments, String accountTo) {
        this.id = id;
        this.amount = amount;
        this.payments = payments;
        this.accountTo = accountTo;
    }

    public String getId() {
        return id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPayments() {
        return payments;
    }

    public void setPayments(String payments) {
        this.payments = payments;
    }

    public String getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(String accountTo) {
        this.accountTo = accountTo;
    }
}
