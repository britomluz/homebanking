package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.services.impl.AccountServiceImpl;
import com.mindhub.homebanking.services.impl.ClientServiceImpl;
import com.mindhub.homebanking.services.impl.TransactionServiceImpl;
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
public class TransactionController {

    @Autowired
    private TransactionServiceImpl transactionServiceImpl;

    @Autowired
    private AccountServiceImpl accountServiceImpl;

    @Autowired
    private ClientServiceImpl clientServiceImpl;



    @PostMapping("/transactions")
    public ResponseEntity<Object> transfer(Authentication authentication,
                                           @RequestParam String amount, @RequestParam String description,
                                           @RequestParam String originNumberAccount, @RequestParam String destinyNumberAccount ){

        Client client = clientServiceImpl.getByEmail(authentication.getName());

        Account originAccount = accountServiceImpl.getByNumber(originNumberAccount);
        Account destinyAccount = accountServiceImpl.getByNumber(destinyNumberAccount);
        double amountTransaction = Double.parseDouble(amount);

        if (amount.isEmpty() || description.isEmpty()) {
            return new ResponseEntity<>("El monto o la descripcion no pueden estar vacios", HttpStatus.BAD_REQUEST);
        }

        if (originNumberAccount.isEmpty() || destinyNumberAccount.isEmpty()) {
            return new ResponseEntity<>("Los numeros de cuenta no pueden estar vacios", HttpStatus.FORBIDDEN);
        }

        if (amountTransaction < 15) {
            return new ResponseEntity<>("El monto minimo para transferir es de $50", HttpStatus.FORBIDDEN);
        }
        if (amountTransaction > 50000) {
            return new ResponseEntity<>("El monto máximo por transacción es de $50000", HttpStatus.FORBIDDEN);
        }

        if (originAccount == null) {
            return new ResponseEntity<>("Por favor, elige una cuenta de origen correcta", HttpStatus.FORBIDDEN);
        }

        if (destinyAccount == null) {
            return new ResponseEntity<>("La cuenta a la que quiere transferir no existe", HttpStatus.FORBIDDEN);
        }

        if (!client.getAccounts().stream().map(Account::getNumber).collect(Collectors.toList()).contains(originNumberAccount)){
            return new ResponseEntity<>("Cuenta inexistente", HttpStatus.FORBIDDEN);
        }

        if (originAccount.getBalance() < amountTransaction){
            return new ResponseEntity<>("Saldo insuficiente", HttpStatus.FORBIDDEN);
        }

        if(originAccount == destinyAccount){
            return new ResponseEntity<>("La cuenta de destino no puede ser igual a la cuenta de origen", HttpStatus.FORBIDDEN);
        }

        Transaction newTransaction = new Transaction(TransactionType.DEBITO, amountTransaction, description + " - A cuenta Nº "  + destinyAccount.getNumber(), LocalDateTime.now());
        transactionServiceImpl.save(newTransaction);
        originAccount.addTransaction(newTransaction);

        double newBalance = originAccount.getBalance() - amountTransaction;
        originAccount.setBalance(newBalance);
        accountServiceImpl.save(originAccount);

        Transaction newTransactionC = new Transaction(TransactionType.CREDITO, amountTransaction, description + " - De cuenta Nº  "  + originAccount.getNumber(), LocalDateTime.now());
        transactionServiceImpl.save(newTransactionC);
        destinyAccount.addTransaction(newTransactionC);

        double newBalanceD = destinyAccount.getBalance() + amountTransaction;
        destinyAccount.setBalance(newBalanceD);
        accountServiceImpl.save(destinyAccount);

        return new ResponseEntity<Object>("La transaccion se realizó con éxito"  , HttpStatus.CREATED);
    }


}
