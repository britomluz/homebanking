package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.services.impl.MailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SendMailController {

    @Autowired
    private MailServiceImpl mailServiceImpl;

    @GetMapping("/")
    public String index(){
        return "send_mail_view";
    }

    @PostMapping("/sendMail")
    public String sendMail(@RequestParam("name") String name, @RequestParam("mail") String mail, @RequestParam("subject") String subject, @RequestParam("body") String body){

        String message = body +"\n\n\nDatos de contacto: " + "\nNombre: " + name + "\nE-mail: " + mail;
        mailServiceImpl.sendMail("mailst664@gmail.com", subject, message);

        return "send_mail_view";
    }
}
