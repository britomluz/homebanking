package com.mindhub.homebanking.services;

public interface MailService {
    public void sendMail( String to, String subject, String body);
}
