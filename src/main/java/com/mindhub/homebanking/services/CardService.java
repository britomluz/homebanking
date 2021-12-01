package com.mindhub.homebanking.services;


import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;

import java.util.List;
import java.util.Optional;


public interface CardService {
    public List<Card> getAll();
    public Card save(Card card);
    public Optional<Card> getById(Long id);
    public void deleteCard(Long id);
    Card getByNumber(String number);

}
