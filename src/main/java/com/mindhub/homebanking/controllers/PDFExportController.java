package com.mindhub.homebanking.controllers;

import com.lowagie.text.DocumentException;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.PDFGeneratorService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api")
public class PDFExportController {

    @Autowired
    private PDFGeneratorService pdfGeneratorService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/pdf/accounts")
    public ResponseEntity<Object> exportAccountsToPDF(HttpServletResponse response, Authentication authentication) throws DocumentException, IOException {

        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=mbb_cl_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        // Client client = clientService.getByEmail("melba@mindhub.com");
        Client client = clientService.getByEmail(authentication.getName());

        List<AccountDTO> listAccounts = client.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toList());

        this.pdfGeneratorService.exportAccount(response, listAccounts, client);

        return new ResponseEntity<>("PDF creado con éxito", HttpStatus.CREATED);
    }

    @PostMapping("/pdf/transfer")
    public ResponseEntity<Object> exportTransfer(HttpServletResponse response, Authentication authentication, @RequestParam Long id) throws DocumentException, IOException {

        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=mbb_tr_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        Client client = clientService.getByEmail(authentication.getName());

       // List<AccountDTO> listAccounts = client.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toList());

        //find transaction by id param
        Transaction transfer = transactionService.getById(id).orElse(null);

        //find the account asociated to transfer and get the id
        Account account = accountService.getById(transfer.getAccount().getId()).orElse(null);

        this.pdfGeneratorService.exportTransfer(response, transfer,account, client);

        return new ResponseEntity<>("PDF creado con éxito", HttpStatus.CREATED);
    }

    @PostMapping("/pdf/transaction")
    public ResponseEntity<Object> exportTransactionToPDF(HttpServletResponse response, Authentication authentication,
                                                         @RequestParam String amount,
                                                         @RequestParam String description,
                                                         @RequestParam String originNumberAccount,
                                                         @RequestParam String destinyNumberAccount) throws DocumentException, IOException {

        response.setContentType("application/pdf");

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=mbb_tr_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);


        Client client = clientService.getByEmail(authentication.getName());

        this.pdfGeneratorService.exportTransaction(response, client, amount, description, originNumberAccount, destinyNumberAccount);

        return new ResponseEntity<>("PDF creado con éxito", HttpStatus.CREATED);

    }

    @PostMapping("/pdf/listoftransfer")
    public ResponseEntity<Object> exportTransactionToPDF(HttpServletResponse response,
                                                         Authentication authentication,
                                                         @RequestParam String from,
                                                         @RequestParam String to,
                                                         @RequestParam Long accountId) throws DocumentException, IOException, ParseException {

        response.setContentType("application/pdf");

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=mbb_tr_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        LocalDateTime fromm = LocalDateTime.parse(from);
        LocalDateTime too = LocalDateTime.parse(to);

        Client client = clientService.getByEmail(authentication.getName());
        Account accountN = accountService.getById(accountId).orElse(null);
        List<Transaction> listTransfers = transactionService.getAllByDateBetween(fromm, too).stream().filter(transaction -> transaction.getAccount().getId().equals(accountId)).collect(Collectors.toList());

        if(from.isEmpty() || to.isEmpty() || accountId.toString().isEmpty()){
            return new ResponseEntity<>("Los campos no pueden estar vacíos", HttpStatus.CONFLICT);
        }

        //verify that "to" and "from" date couldn't be before actual date
        if(too.isAfter(LocalDateTime.now().plusHours(10))|| fromm.isAfter(LocalDateTime.now().plusHours(10))){
            return new ResponseEntity<>("La fecha ingresada debe ser menor a la fecha actual", HttpStatus.FORBIDDEN);
        }

        if(listTransfers.size() == 0){
            return new ResponseEntity<>("No se encontró ninguna transferencia en ese rango de fechas", HttpStatus.LENGTH_REQUIRED);
        }

        this.pdfGeneratorService.exportListTransactions(response, listTransfers, client, fromm, too, accountN);

        return new ResponseEntity<>("PDF creado con éxito", HttpStatus.CREATED);
    }
/*
    @GetMapping("/pdf/generate")
    public void generatePDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy:HH:mm");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);


        this.pdfGeneratorService.export(response);
    }*/

}
