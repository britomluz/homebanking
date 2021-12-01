package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CardDTO {
    private Long id;
    private CardType type;
    private CardColor color;
    private String cardholder;
    private String number;
    private int cvv;
    private LocalDateTime fromDate;
    private LocalDateTime thruDate;
    private Set<PayDTO> pays = new LinkedHashSet<>();

    public CardDTO() { }

    public CardDTO(Card card) {
        this.id = card.getId();
        this.type = card.getType();
        this.color = card.getColor();
        this.cardholder = card.getCardholder();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
        this.pays = card.getPays().stream().map(pay -> new PayDTO(pay)).collect(Collectors.toSet());
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

    public String getCardholder() {
        //String cardholderUpper = client.getFirstName().toUpperCase() + " " + client.getLastName().toUpperCase()
        String cardholderUpper;
        return cardholder;
    }

    public void setCardholder(String cardholder) {
        this.cardholder = cardholder;
    }

    public String getNumber() {
        //Integer a String // String cadena = String.valueOf(entero);   String cadena = Integer.toString(entero);
        //String a Integer // Integer entero = Integer.valueOf(cadena); int entero = Integer.parseInt(cadena);

        //String numberCard = number.toString().substring(0, 4) + " " + number.toString().substring(4, 8) + " " + number.toString().substring(8, 12) + " " + number.toString().substring(12, 16);
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getCvv() {return cvv;}

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public String getFromDate() {
        DateTimeFormatter dateFormatt = DateTimeFormatter.ofPattern("MM/yy");
        String dateFromString = fromDate.format(dateFormatt);
        return dateFromString;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public String getThruDate() {
        DateTimeFormatter dateFormatt = DateTimeFormatter.ofPattern("MM/yy");
        String dateString = thruDate.format(dateFormatt);
        return dateString;
    }

    public void setThruDate(LocalDateTime thruDate) {
        this.thruDate = thruDate;
    }

    public Set<PayDTO> getPays() {
        return pays;
    }

    public void setPays(Set<PayDTO> pays) {
        this.pays = pays;
    }
}
