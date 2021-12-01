package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.LoanService;
import com.mindhub.homebanking.services.TransactionService;
import com.mindhub.homebanking.services.impl.AccountServiceImpl;
import com.mindhub.homebanking.services.impl.ClientServiceImpl;
import com.mindhub.homebanking.services.impl.LoanServiceImpl;
import com.mindhub.homebanking.services.impl.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AdminController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientServiceImpl clientService;

    @Autowired
    private AccountServiceImpl accountService;

    @Autowired
    private TransactionServiceImpl transactionService;

    @Autowired
    private  LoanServiceImpl loanService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //add client

    @PostMapping("/admin/clients") //ruta para obtener los clients y usar el metodo POST
    public ResponseEntity<Object> addClient(Authentication authentication,
                                            @RequestParam String firstName, @RequestParam String lastName,
                                            @RequestParam String email,  @RequestParam String password, String rol) {

        Client clientAdmin = clientService.getByEmail(authentication.getName());

        if(!clientAdmin.getRole().equals(ClientType.ADMIN)){
            return new ResponseEntity<>("Permisos insuficientes", HttpStatus.FORBIDDEN);
        }

        ClientType role = ClientType.valueOf(rol);

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || rol.isEmpty()) {
            return new ResponseEntity<>("El formulario de registro tiene campos vacíos", HttpStatus.BAD_REQUEST);
        }

        if (clientService.getByEmail(email) != null) {
            return new ResponseEntity<>("Ya existe un usuario con el email ingresado. Intentalo de nuevo.", HttpStatus.FORBIDDEN);
        }
        String n = numberAccount();

        while(accountService.getAll().stream().map(Account::getNumber).collect(Collectors.toList()).contains(n)){
            n = numberAccount();
        }

        Client client = clientService.save(new Client(firstName, lastName, email, passwordEncoder.encode(password), "http://localhost:8080/web/assets/photoUser.png", role));

        Account newAccount = new Account(n, LocalDateTime.now(), AccountType.CAJADEAHORRO, 0.00, client);
        accountService.save(newAccount);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    //delete clients accounts
    @DeleteMapping("/admin/accounts/delete/{number}") //ruta para obtener las cuentas y usar el metodo POST
    public ResponseEntity<Object> deleteAccount(Authentication authentication, @PathVariable String number){

        Client client = clientService.getByEmail(authentication.getName());
        Account account = accountService.getByNumber(number);
        Set<Transaction> transactions = account.getTransactions();

        //verify if account is in account repository
        if(!client.getRole().equals(ClientType.ADMIN)){
            return new ResponseEntity<>("No tienes permisos suficientes para eliminar una cuenta", HttpStatus.FORBIDDEN);
        }

        if(!accountService.getAll().stream().map(account1 -> account1.getNumber()).collect(Collectors.toList()).contains(number)){
            return new ResponseEntity<>("La cuenta no existe", HttpStatus.FORBIDDEN);
        }
        if(account.getBalance() > 0){
            return new ResponseEntity<>("Para eliminar la cuenta no debe poseer un saldo mayor a cero", HttpStatus.FORBIDDEN);
        }

        //transactionRepository.deleteAll(transactions);
        transactionService.deleteAllTransactions(transactions);
        accountService.deleteAccount(account);

        return new ResponseEntity<>("Cuenta eliminada con éxito", HttpStatus.OK);
    }

    public String numberAccount(){
        int num = (int) (Math.random()*(99999999-11111111) + 11111111);
        System.out.println(num);
        return "VIN"+num;
    }

    @PostMapping("/admin/newloans")
    public ResponseEntity<Object> createLoans(Authentication authentication,
                                              @RequestParam String name,
                                              @RequestParam String maxAmount,
                                              @RequestParam String payments,
                                              @RequestParam String interest){

        Client client = clientService.getByEmail(authentication.getName());

        double maxAmnt = Double.parseDouble(maxAmount);
        List<Integer> paymnts = Arrays.asList(payments.split(",")).stream().map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
        int interestLoan = Integer.parseInt(interest);

        if(!client.getRole().equals(ClientType.ADMIN)){
            return new ResponseEntity<>("No tienes permisos suficientes para crear un nuevo préstamo", HttpStatus.FORBIDDEN);
        }

        if (name.isEmpty() || maxAmount.isEmpty() || payments.isEmpty() || interest.isEmpty()) {
            return new ResponseEntity<>("Por favor, rellene todos los campos", HttpStatus.BAD_REQUEST);
        }


        if(loanService.getAll().stream().map(loan -> loan.getName()).collect(Collectors.toList()).contains(name)){
            return new ResponseEntity<>("Ya hay un prestamo con ese nombre", HttpStatus.FORBIDDEN);
        }


        Loan newLoan = new Loan(name, maxAmnt, paymnts, interestLoan);
        loanService.save(newLoan);


        return new ResponseEntity<>("Préstamo creado con éxito", HttpStatus.CREATED);
    }


    @PatchMapping("/admin/clients/edit") // edit client
    public ResponseEntity<Object> editClient(Authentication authentication,
                                             @RequestParam String Client){

        Client clientAdmin = clientService.getByEmail(authentication.getName());

        if(!clientAdmin.getRole().equals(ClientType.ADMIN)){
            return new ResponseEntity<>("Permisos insuficientes", HttpStatus.FORBIDDEN);
        }


        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    @PatchMapping("/api/shop") // edit client
    public ResponseEntity<Object> shop(Authentication authentication,
                                       @RequestParam String firstName,
                                       @RequestParam String lastName,
                                       @RequestParam String number,
                                       @RequestParam String cvv,
                                       @RequestParam String thruDate,
                                       @RequestParam String adress){

        Client clientAdmin = clientService.getByEmail(authentication.getName());

        if(!clientAdmin.getRole().equals(ClientType.ADMIN)){
            return new ResponseEntity<>("Permisos insuficientes", HttpStatus.FORBIDDEN);
        }


        return new ResponseEntity<Object>(HttpStatus.OK);
    }

}
