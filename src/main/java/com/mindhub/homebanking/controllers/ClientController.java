package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")//le indica que inicie en /api y los request mapping dentro de la clase inician todos en /api
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder; //cifrado de la contraseña

    @RequestMapping("/clients")// no hace falta poner /api/clients
    public List<ClientDTO> getClients() {
        return clientService.getAll().stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());
    }


    @RequestMapping("/clients/current")
    public ClientDTO getClients(Authentication authentication) {
        return new ClientDTO(clientService.getByEmail(authentication.getName()));
    }

    //verify that card owns authenticated client

    @PostMapping("/clients") //ruta para obtener los clients y usar el metodo POST
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email,  @RequestParam String password) {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("El formulario de registro tiene campos vacíos", HttpStatus.BAD_REQUEST);
        }

        if (clientService.getByEmail(email) != null) {
            return new ResponseEntity<>("Ya existe un usuario con el email ingresado. Intentalo de nuevo.", HttpStatus.FORBIDDEN);
        }
        String n = numberAccount();

        while(accountService.getAll().stream().map(Account::getNumber).collect(Collectors.toList()).contains(n)){
            n = numberAccount();
        }

        Client client = clientService.save(new Client(firstName, lastName, email, passwordEncoder.encode(password), "https://p16-va-default.akamaized.net/img/musically-maliva-obj/1665282759496710~c5_720x720.jpeg", ClientType.CLIENT));

        Account newAccount = new Account(n, LocalDateTime.now(), AccountType.CAJADEAHORRO, 0.00, client);
        accountService.save(newAccount);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public String numberAccount(){
        int num = (int) (Math.random()*(99999999-11111111) + 11111111);
        System.out.println(num);
        return "VIN-"+ num;
    }

    @PatchMapping("/clients/current")
    public ResponseEntity<Object> modificar(Authentication authentication,
                                            @RequestParam String email) {

        Client client = clientService.getByEmail(authentication.getName());

        if (email.isEmpty()) {
            return new ResponseEntity<>("Por favor, ingresá un email válido", HttpStatus.BAD_REQUEST);
        }

        client.setEmail(email);

        clientService.save(client);
        return new ResponseEntity<>(HttpStatus.OK);
    };

    @PatchMapping("/clients/current/photo")
    public ResponseEntity<Object> editPhoto(Authentication authentication,
                                            @RequestParam String url) {

        Client client = clientService.getByEmail(authentication.getName());

        if (url.isEmpty()) {
            return new ResponseEntity<>("No hay imagen cargada", HttpStatus.BAD_REQUEST);
        }
        client.setUrl(url);
        clientService.save(client);
        return new ResponseEntity<>(HttpStatus.OK);
    };


    // admin requests
    @RequestMapping("/admin/clientss/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return clientService.getById(id).map(ClientDTO::new).orElse(null);
    }

    //verify that card owns authenticated client
    @GetMapping("/admin/clients/{id}")
    public ResponseEntity<Object> getAccounts(Authentication authentication, @PathVariable Long id){

        Client clientAdmin = clientService.getByEmail(authentication.getName());

        if(!clientAdmin.getRole().equals(ClientType.ADMIN)){
            return new ResponseEntity<>("Permisos insuficientes", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>( clientService.getById(id).map(ClientDTO::new).orElse(null), HttpStatus.CREATED);
    }



}
