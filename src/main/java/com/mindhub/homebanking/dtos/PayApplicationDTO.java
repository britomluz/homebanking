package com.mindhub.homebanking.dtos;

import org.springframework.web.bind.annotation.RequestParam;

public class PayApplicationDTO {
    private String name;
    private String number;
    private String cvv;
    private String adress;
    private String payments;
    private String total;
    private String description;

    public PayApplicationDTO() { }

    public PayApplicationDTO(String name, String number, String cvv, String adress, String payments, String total, String description) {
        this.name = name;
        this.number = number;
        this.cvv = cvv;
        this.adress = adress;
        this.payments = payments;
        this.total = total;
        this.description = description;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPayments() {
        return payments;
    }

    public void setPayments(String payments) {
        this.payments = payments;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
