package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.PayApplicationDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.PayService;
import com.mindhub.homebanking.services.impl.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Transactional
@RestController
@RequestMapping("/api")

public class PayController {



    @Autowired
    private CardServiceImpl cardServiceImpl;

    @Autowired
    private ClientServiceImpl clientServiceImpl;

    @Autowired
    private PayServiceImpl payServiceImpl;

    @Autowired
    private TransactionServiceImpl transactionServiceImpl;

    @Autowired
    private AccountServiceImpl accountServiceImpl;

    @Autowired
    private MovementServiceImpl movementServiceImpl;

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/clients/current/pays")
    public ResponseEntity<Object> transfer(Authentication authentication,
                                           @RequestParam String description,
                                           @RequestParam String amount,
                                           @RequestParam String payments,
                                           @RequestParam String cardNumber,
                                           @RequestParam String numberAccount) {

        Client client = clientServiceImpl.getByEmail(authentication.getName());
        Card card = cardServiceImpl.getByNumber(cardNumber);
        //Card card = cardServiceImpl.findByNumber(cardNumber);
        Account account = card.getAccount();

        Account accountDest = accountServiceImpl.getByNumber(numberAccount);

        if (description.isEmpty() || amount.isEmpty() || payments.isEmpty() || cardNumber.isEmpty()) {
            return new ResponseEntity<>("Por favor, rellene todos los campos", HttpStatus.BAD_REQUEST);
        }
        //verify if account is in account repository
        if(!cardServiceImpl.getAll().stream().map(card1 -> card1.getNumber()).collect(Collectors.toList()).contains(cardNumber)){
            return new ResponseEntity<>("La tarjeta con la que intenta pagar no existe", HttpStatus.FORBIDDEN);
        }

        if(!client.getCards().stream().map(card1 -> card1.getNumber()).collect(Collectors.toList()).contains(cardNumber)){
            return new ResponseEntity<Object>("La tarjeta con ese id no es de su propiedad", HttpStatus.OK);
        }

        LocalDateTime dateNow = LocalDateTime.now();
        if(card.getThruDate().isBefore(dateNow)){
            return new ResponseEntity<>("Su tarjeta está vencida", HttpStatus.FORBIDDEN);
        }

        Integer paymnts = Integer.parseInt(payments);
        double amountt = Double.parseDouble(amount);

        if(card.getType().equals(CardType.DEBITO)){
            if (account.getBalance() < amountt){
                return new ResponseEntity<>("Saldo insuficiente", HttpStatus.FORBIDDEN);
            }

            if(amountt > 50000){
                return new ResponseEntity<Object>("El monto excede el máximo permitido en un pago", HttpStatus.FORBIDDEN);
            }

            if(paymnts != 1){
                return new ResponseEntity<Object>("Con tarjeta de débito se permite un solo pago", HttpStatus.FORBIDDEN);
            }
            Pay newPay = new Pay(LocalDateTime.now(), description, Double.parseDouble(amount), 1,  Double.parseDouble(amount), card);
            payServiceImpl.save(newPay);

            Transaction newTransaction = new Transaction(TransactionType.DEBITO, amountt, description + "DEBITO DE COMPRA" , LocalDateTime.now());
            transactionServiceImpl.save(newTransaction);
            account.addTransaction(newTransaction);

            double newBalance = account.getBalance() - amountt;
            account.setBalance(newBalance);
            accountServiceImpl.save(account);

            Transaction newTransactionCred = new Transaction(TransactionType.CREDITO, amountt, description + "Credito de " + account.getNumber() , LocalDateTime.now());
            transactionServiceImpl.save(newTransactionCred);
            accountDest.addTransaction(newTransactionCred);

            double newBalanceTwo = accountDest.getBalance() + amountt;
            accountDest.setBalance(newBalanceTwo);
            accountServiceImpl.save(accountDest);
        }

        if (card.getType().equals(CardType.CREDITO)){

            if(paymnts != 3 && paymnts != 6 && paymnts != 12 && paymnts != 18 && paymnts != 24){
                return new ResponseEntity<Object>("La cantidad de cuotas ingresadas es incorrecta", HttpStatus.FORBIDDEN);
            }
            if (card.getColor().equals(CardColor.SILVER) && amountt > 100000){
                return new ResponseEntity<>("Saldo insuficiente", HttpStatus.FORBIDDEN);
            }
            if (card.getColor().equals(CardColor.GOLD) && amountt > 180000){
                return new ResponseEntity<>("Saldo insuficiente", HttpStatus.FORBIDDEN);
            }
            if(paymnts == 3){
                 Double amnt = amountt * 1.10;

                Pay newPay = new Pay(LocalDateTime.now(), description, amnt, paymnts,   amnt / paymnts, card);
                payServiceImpl.save(newPay);
            }

            if(paymnts == 6){
                Double amnt = amountt * 1.15;

                Pay newPay = new Pay(LocalDateTime.now(), description, amnt, paymnts,   amnt / paymnts, card);
                payServiceImpl.save(newPay);
            }

            if(paymnts == 12){
                Double amnt = amountt * 1.30;

                Pay newPay = new Pay(LocalDateTime.now(), description, amnt, paymnts,   amnt / paymnts, card);
                payServiceImpl.save(newPay);
            }
            if(paymnts == 24){
                Double amnt = amountt * 1.45;

                Pay newPay = new Pay(LocalDateTime.now(), description, amnt, paymnts,   amnt / paymnts, card);
                payServiceImpl.save(newPay);
            }
        }

        return new ResponseEntity<Object>("Pago realizado con éxito", HttpStatus.OK);
    }

    ///clients/current/pays
    @PostMapping("/clients/current/card/pays")
    public ResponseEntity<Object> transfer(Authentication authentication,
                                           @RequestParam String totalPay,
                                           @RequestParam String numberAccount,
                                           @RequestParam Long id){

        Client client = clientServiceImpl.getByEmail(authentication.getName());
        Account account = accountServiceImpl.getByNumber(numberAccount);
        Card card = cardServiceImpl.getById(id).orElse(null);

        Double pay = Double.parseDouble(totalPay);

        if (totalPay.isEmpty() || numberAccount.isEmpty()) {
            return new ResponseEntity<>("Para pagar selecciona una cuenta", HttpStatus.FORBIDDEN);
        }

        if(pay <= 0 ){
            return new ResponseEntity<>("El total no puede ser pagado.", HttpStatus.FORBIDDEN);
        }

        if(account.getBalance() < pay){

            return new ResponseEntity<>("Saldo insuficiente. Por favor selecciona otra cuenta.", HttpStatus.FORBIDDEN);
        }

        if (!client.getAccounts().stream().map(Account::getNumber).collect(Collectors.toList()).contains(numberAccount)){
            return new ResponseEntity<>("Cuenta inexistente", HttpStatus.FORBIDDEN);
        }

        if(!client.getCards().stream().map(Card::getId).collect(Collectors.toList()).contains(id)){
            return new ResponseEntity<>("La tarjeta que quiere pagar no le pertenece", HttpStatus.FORBIDDEN);
        }

        Movement newMovement = new Movement("Pago de resumen de tarjeta "+ card.getType()+ " " + card.getColor(), LocalDateTime.now(), client);
        movementServiceImpl.save(newMovement);

        Transaction newTransaction = new Transaction(TransactionType.DEBITO, pay, "Pago de resumen de tarjeta "+ card.getType()+ " " + card.getColor(), LocalDateTime.now());
        transactionServiceImpl.save(newTransaction);
        account.addTransaction(newTransaction);

        double newBalance = account.getBalance() - pay;
        account.setBalance(newBalance);
        accountServiceImpl.save(account);

        return new ResponseEntity<Object>("Pago realizado con éxito", HttpStatus.OK);
    }

    @PostMapping("/shop")
    public ResponseEntity<Object> shop(@RequestBody PayApplicationDTO payApplicationDTO){

        String number = payApplicationDTO.getNumber();
        String numberCard = number.substring(0, 4) + " " + number.substring(4, 8) + " " + number.substring(8, 12) + " " + number.substring(12, 16);
        Card card = cardServiceImpl.getByNumber(numberCard);
       // double pay = Double.parseDouble(payApplicationDTO.getTotal());
        //int paymnts = Integer.parseInt(payApplicationDTO.getPayments());

        int paymnts = Integer.parseInt(payApplicationDTO.getPayments());
        double amountt = Double.parseDouble(payApplicationDTO.getTotal());


        if (payApplicationDTO.getName().isEmpty() || payApplicationDTO.getNumber().isEmpty() || payApplicationDTO.getDescription().isEmpty()
                || payApplicationDTO.getCvv().isEmpty() || payApplicationDTO.getAdress().isEmpty() || payApplicationDTO.getTotal().isEmpty() || payApplicationDTO.getPayments().isEmpty()) {
            return new ResponseEntity<>("Los campos no pueden estar vacíos", HttpStatus.FORBIDDEN);
        }

        //if card is not in card repository
        if(card == null){
            return new ResponseEntity<>("La tarjeta no existe", HttpStatus.FORBIDDEN);
        }
        /*
        if(!client.getCards().stream().map(card1 -> card1.getNumber()).collect(Collectors.toList()).contains(payApplicationDTO.getNumber())){
            return new ResponseEntity<>("La tarjeta no es de su propiedad", HttpStatus.FORBIDDEN);
        }*/

        LocalDateTime dateNow = LocalDateTime.now();
        if(dateNow.isAfter(card.getThruDate())){
            return new ResponseEntity<>("La tarjeta esta vencida", HttpStatus.FORBIDDEN);
        }

        int cardCvv = Integer.parseInt(payApplicationDTO.getCvv());
        if(card.getCvv() != cardCvv){
            return new ResponseEntity<>("El CVV es incorrecto", HttpStatus.FORBIDDEN);
        }

       //shop with debit card
        if(card.getType().equals(CardType.DEBITO)){
            Account account = accountServiceImpl.getByNumber(card.getAccount().getNumber());
            String description = payApplicationDTO.getDescription();

            if (account.getBalance() < amountt){
                return new ResponseEntity<>("Saldo insuficiente", HttpStatus.FORBIDDEN);
            }

            if(amountt > 50000){
                return new ResponseEntity<Object>("El monto excede el máximo permitido en un pago", HttpStatus.FORBIDDEN);
            }

            if(paymnts != 1){
                return new ResponseEntity<Object>("Con tarjeta de débito se permite un solo pago", HttpStatus.FORBIDDEN);
            }
            Pay newPay = new Pay(LocalDateTime.now(), description, amountt, 1,  amountt, card);
            payServiceImpl.save(newPay);

            Transaction newTransaction = new Transaction(TransactionType.DEBITO, amountt, description + "DEBITO DE COMPRA" , LocalDateTime.now());
            transactionServiceImpl.save(newTransaction);
            account.addTransaction(newTransaction);

            double newBalance = account.getBalance() - amountt;
            account.setBalance(newBalance);
            accountServiceImpl.save(account);
        }

        //shop with credit card
        if (card.getType().equals(CardType.CREDITO)){
            String description = payApplicationDTO.getDescription();

            if(paymnts != 3 && paymnts != 6 && paymnts != 12 && paymnts != 18 && paymnts != 24){
                return new ResponseEntity<Object>("La cantidad de cuotas ingresadas es incorrecta", HttpStatus.FORBIDDEN);
            }
            if (card.getColor().equals(CardColor.SILVER) && amountt > 100000){
                return new ResponseEntity<>("Saldo insuficiente", HttpStatus.FORBIDDEN);
            }
            if (card.getColor().equals(CardColor.GOLD) && amountt > 180000){
                return new ResponseEntity<>("Saldo insuficiente", HttpStatus.FORBIDDEN);
            }
            if(paymnts == 3){
                Double amnt = amountt * 1.10;

                Pay newPay = new Pay(LocalDateTime.now(), description, amnt, paymnts,   amnt / paymnts, card);
                payServiceImpl.save(newPay);
            }

            if(paymnts == 6){
                Double amnt = amountt * 1.15;

                Pay newPay = new Pay(LocalDateTime.now(), description, amnt, paymnts,   amnt / paymnts, card);
                payServiceImpl.save(newPay);
            }

            if(paymnts == 12){
                Double amnt = amountt * 1.30;

                Pay newPay = new Pay(LocalDateTime.now(), description, amnt, paymnts,   amnt / paymnts, card);
                payServiceImpl.save(newPay);
            }
            if(paymnts == 24){
                Double amnt = amountt * 1.45;

                Pay newPay = new Pay(LocalDateTime.now(), description, amnt, paymnts,   amnt / paymnts, card);
                payServiceImpl.save(newPay);
            }
        }

        return new ResponseEntity<>("Compra exitosa", HttpStatus.OK);
    }


}
