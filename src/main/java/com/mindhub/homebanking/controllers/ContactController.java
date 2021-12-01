package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ContactController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ContactService contactService;



    @PostMapping("/clients/current/contacts")
    public ResponseEntity<Object> createContact(Authentication authentication,
                                                @RequestParam String nameContact,
                                                @RequestParam String accountContact,
                                                @RequestParam String type){

        Client client = clientService.getByEmail(authentication.getName());


        if (nameContact.isEmpty() || accountContact.isEmpty() || type.isEmpty()) {
            return new ResponseEntity<>("Los campos no pueden estar vacios", HttpStatus.BAD_REQUEST);
        }

        ContactType typeContact = ContactType.valueOf(type);

        //can't own more than 10 contact favs
        if (client.getContacts().stream().filter(contact -> contact.getType().equals(ContactType.FAV)).count() > 9 ) {
            return new ResponseEntity<>("No se puede agregar más de 10 contactos a favoritos", HttpStatus.FORBIDDEN);
        }

        //si el nameContact ya existe - >
        if(client.getContacts().stream().map(Contact::getNameContact).collect(Collectors.toList()).contains(nameContact)){
            return new ResponseEntity<>("Ya existe un contacto agendado con ese nombre.", HttpStatus.FORBIDDEN);
        }

        //si la accountContact ya existe - > error
        if(client.getContacts().stream().map(Contact::getAccountContact).collect(Collectors.toList()).contains(accountContact)){
            return new ResponseEntity<>("Ya existe un contacto agendado con esa cuenta.", HttpStatus.FORBIDDEN);
        }


        Contact newContact = new Contact(nameContact, accountContact, typeContact , client);
        contactService.save(newContact);
        return new ResponseEntity<>("Contacto agendado con éxito", HttpStatus.CREATED);

    };

    @PatchMapping("/clients/contacts/edit")
    public ResponseEntity<Object> modificar( @RequestParam String accountContact
        //                                        @RequestParam String type
    ) {

        Contact contact = contactService.getByAccountContact(accountContact);

        if (accountContact.isEmpty()) {
            return new ResponseEntity<>("El campo de la cuenta no puede estar vacío", HttpStatus.BAD_REQUEST);
        }

        // si el type de este contacto es fav -> cambialo a nofav
        if(contact.getType() == ContactType.FAV){
            contact.setType(ContactType.NOFAV);
            contactService.save(contact);
            return new ResponseEntity<>("Movido a 'no favoritos'", HttpStatus.OK);
        }

        // si el type de este contacto es nofav -> cambialo a fav
        if(contact.getType() == ContactType.NOFAV){
            contact.setType(ContactType.FAV);
            contactService.save(contact);
            return new ResponseEntity<>("Movido a 'favoritos'", HttpStatus.OK);
        }

        //Account destinyAccount = accountService.getByNumber(destinyNumberAccount);
        //if (type.isEmpty()) {
        //    return new ResponseEntity<>("Por favor, ingresá un email válido", HttpStatus.BAD_REQUEST);
        //}
        //ContactType contactType = ContactType.valueOf(type);
        //contact.setType(contactType);
        //contactService.save(contact);

        return new ResponseEntity<>("Contacto editado exitosamente",HttpStatus.OK);
    };
}
