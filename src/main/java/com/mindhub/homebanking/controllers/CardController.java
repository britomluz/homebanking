package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.MovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private CardService cardService;

    @Autowired
    private MovementService movementService;

    @Autowired
    private AccountService accountService;

    // this ways allow every client to see every card
    /*
    @RequestMapping("/card/{id}")
    public CardDTO getCards(@PathVariable Long id){
        return cardService.getById(id).map(CardDTO::new).orElse(null);
    }*/

    //verify that card owns authenticated client
    @GetMapping("/clients/current/cards/{id}")
    public ResponseEntity<Object> getCards(Authentication authentication, @PathVariable Long id){

        Client client = clientService.getByEmail(authentication.getName());
        Card card = cardService.getById(id).orElse(null);

        if(!client.getCards().contains(card)){
            return new ResponseEntity<>("La tarjeta no le pertenece",HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(cardService.getById(id).map(CardDTO::new).orElse(null), HttpStatus.CREATED);
    }

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(
            Authentication authentication,
            @RequestParam String color,
            @RequestParam String type,
            @RequestParam String numberAccount) {


        Client client = clientService.getByEmail(authentication.getName());
        Account account = accountService.getByNumber(numberAccount);
        CardColor cardColor = CardColor.valueOf(color);
        CardType cardType = CardType.valueOf(type);


        if (color.isEmpty() || type.isEmpty()) {
            return new ResponseEntity<>("Debes seleccionar bien los campos. Por favor, intentalo de nuevo.", HttpStatus.BAD_REQUEST);
        }

        if (client.getCards().stream().filter(card -> card.getType().equals(cardType)).count() > 2 ) {

            return new ResponseEntity<>("No se puede solicitar mas de tres tarjetas de " + type, HttpStatus.FORBIDDEN);
        }

        if (client.getCards().stream().filter(card -> card.getType().equals(cardType)).filter(card -> card.getColor().equals(cardColor)).count() > 0) {

                Movement newMovement = new Movement(" Solicitud de nueva tarjeta rechazada. Motivo: 'Usted ya posee una tarjeta de " + type +" "+ color +"'", LocalDateTime.now(), client);
                movementService.save(newMovement);

                return new ResponseEntity<>(" Usted ya posee una tarjeta de " + type +" "+ color, HttpStatus.CONFLICT);
        }

        String n = numberCard();
        while(cardService.getAll().stream().map(Card::getNumber).collect(Collectors.toList()).contains(n)){
            n = numberCard();
        }

        if (cardType.equals(CardType.DEBITO)){
            if (numberAccount.isEmpty() ) {
                return new ResponseEntity<>("Ingresa una cuenta para asociar tu tarjeta.", HttpStatus.BAD_REQUEST);
            }

            if(account.getCard() == null){

                Card newCard = new Card(cardType, cardColor, client.getFirstName().toUpperCase()+" "+client.getLastName().toUpperCase(), numberCard(), cvvCard(), LocalDateTime.now(), LocalDateTime.now().plusYears(5), client);
                cardService.save(newCard);
                account.addCard(newCard); //asociar a una cuenta si es de debito

                Movement newMovement = new Movement("Tarjeta de "+ type + " " + color + " creada con éxito", LocalDateTime.now(), client);
                movementService.save(newMovement);

                return new ResponseEntity<>("Tarjeta de "+ type + " " + color + " creada con éxito", HttpStatus.CREATED);
            }
            else {
                return new ResponseEntity<>("Esa cuenta ya tiene una tarjeta de débito asociada", HttpStatus.BAD_REQUEST);
            }
        }

        if (cardType.equals(CardType.CREDITO)){
            Card newCard = new Card(cardType, cardColor, client.getFirstName().toUpperCase()+" "+client.getLastName().toUpperCase(), numberCard(), cvvCard(), LocalDateTime.now(), LocalDateTime.now().plusYears(5), client);
            cardService.save(newCard);

            Movement newMovement = new Movement("Tarjeta de "+ type + " " + color + " creada con éxito", LocalDateTime.now(), client);
            movementService.save(newMovement);

            return new ResponseEntity<>("Tarjeta de "+ type + " " + color + " creada con éxito", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("OK", HttpStatus.CREATED);

    }

    public String numberCard(){
        Long number =  (long) (Math.random()*(9999999999999999L-1111111111111111L) + 1111111111111111L);
        String numCard = number.toString().substring(0, 4) + " " + number.toString().substring(4, 8) + " " + number.toString().substring(8, 12) + " " + number.toString().substring(12, 16);
        return numCard;
    }

    public int cvvCard(){
        int num =  (int) (Math.random()*(999-111) + 111);
        return num;
    }

    @DeleteMapping("/clients/current/cards/delete/{id}")
    public ResponseEntity<Object> createCard(Authentication authentication, @PathVariable Long id){
        Client client = clientService.getByEmail(authentication.getName());
        //Long idLoan = Long.parseLong(id);
        //Long ci = Long.parseLong(id)
        Card card = cardService.getById(id).orElse(null);

        String c =  String.valueOf(id);

        if (c.isEmpty()) {
            return new ResponseEntity<>("vacios", HttpStatus.BAD_REQUEST);
        }

        if(!cardService.getAll().stream().map(card1 -> card1.getId()).collect(Collectors.toList()).contains(id)){
            return new ResponseEntity<>("La tarjeta no existe", HttpStatus.FORBIDDEN);
        }

        if(!client.getCards().stream().map(card1 -> card1.getId()).collect(Collectors.toList()).contains(id))  {

            return new ResponseEntity<>("Solo podes eliminar tarjetas de tu propiedad", HttpStatus.FORBIDDEN);
        }

        cardService.deleteCard(id);

        return new ResponseEntity<>("Diste de baja con éxito tu Tarjeta de "+ card.getType() + " " + card.getColor(), HttpStatus.OK);
    }
}