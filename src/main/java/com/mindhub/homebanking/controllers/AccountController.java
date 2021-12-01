package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.*;
import com.mindhub.homebanking.services.impl.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController<UserPDFExporter> {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private MovementService movementService;

    @Autowired
    private PDFGeneratorService pdfGeneratorService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountService.getAll().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/clients/current/accounts/{id}")
    public ResponseEntity<Object> getAccounts(Authentication authentication, @PathVariable Long id){

        Client client = clientService.getByEmail(authentication.getName());
        Account account = accountService.getById(id).orElse(null);

        if(!client.getAccounts().contains(account)){
            return new ResponseEntity<>("La cuenta no le pertenece",HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(accountService.getById(id).map(AccountDTO::new).orElse(null), HttpStatus.CREATED);
    }


    @PostMapping("/clients/current/accounts") //ruta para obtener las cuentas y usar el metodo POST
    public ResponseEntity<Object> createAccount(Authentication authentication, @RequestParam String type){

        Client client = clientService.getByEmail(authentication.getName());

        AccountType accType = AccountType.valueOf(type);

        if(type.isEmpty()){
            return new ResponseEntity<>("Debes seleccionar un tipo de cuenta", HttpStatus.FORBIDDEN);
        }

        if (client.getAccounts().size() > 2){
            Movement newMovement = new Movement("Creación de nueva cuenta rechazada. Motivo: 'No es posible tener mas de 3 cuentas asociadas'", LocalDateTime.now(), client);
            movementService.save(newMovement);
            return new ResponseEntity<>("No es posible tener mas de 3 cuentas asociadas", HttpStatus.FORBIDDEN);
       }

        String n = numberAccount();
        while(accountService.getAll().stream().map(account -> account.getNumber()).collect(Collectors.toList()).contains(n)){
            n = numberAccount();
        }

        Account newAccount = new Account(n, LocalDateTime.now(), accType, 0.00, client);
        accountService.save(newAccount);

        Movement newMovement = new Movement("Nueva cuenta " + accType + " creada exitosamente con el Nº " + newAccount.getNumber(), LocalDateTime.now(), client);
        movementService.save(newMovement);
        //client.addAccount(newAccount);

        return new ResponseEntity<>("Cuenta creada con éxito", HttpStatus.CREATED);

    };

    public String numberAccount(){
        int num = (int) (Math.random()*(99999999-11111111) + 11111111);
        System.out.println(num);
        return "VIN"+num;
    }





}