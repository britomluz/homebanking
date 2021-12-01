package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private MovementService movementService;

    @GetMapping("/loans")
    public List<LoanDTO> getLoans(){
        return loanService.getAll().stream().map(LoanDTO::new).collect(Collectors.toList());
    };


   @Transactional
   @PostMapping("/loans")
   public ResponseEntity<Object> createLoans(Authentication authentication,
                                             @RequestBody LoanApplicationDTO loanApplicationDTO){


       Client client = clientService.getByEmail(authentication.getName());
       Account accountTo = accountService.getByNumber(loanApplicationDTO.getAccountTo());

        Long idLoan = Long.parseLong(loanApplicationDTO.getId());
        double amountLoan = Double.parseDouble(loanApplicationDTO.getAmount());
        int paymentLoan = Integer.parseInt(loanApplicationDTO.getPayments());

       Loan loan = loanService.getById(idLoan).orElse(null);

       if (loanApplicationDTO.getId().isEmpty() || loanApplicationDTO.getAmount().isEmpty() || loanApplicationDTO.getPayments().isEmpty() || loanApplicationDTO.getAccountTo().isEmpty()) {
           return new ResponseEntity<>("Los campos no pueden estar vacíos", HttpStatus.FORBIDDEN);
       }


       if(accountTo == null ){
           return new ResponseEntity<>("La cuenta de destino no existe", HttpStatus.FORBIDDEN);
       }

       if(!client.getAccounts().stream().map(Account::getNumber).collect(Collectors.toList()).contains(accountTo.getNumber())){
           return new ResponseEntity<>("Ud. no es titular de la cuenta de destino", HttpStatus.FORBIDDEN);
       }

       if(loan == null){
           return new ResponseEntity<>("El prestamo no existe", HttpStatus.FORBIDDEN);
       }

       if(amountLoan > loan.getMaxAmount() ){
           return new ResponseEntity<>("El monto solicitado es mayor a lo permitido", HttpStatus.FORBIDDEN);
       }

        if(amountLoan < 10000 ){
            return new ResponseEntity<>("El monto solicitado es menor a lo permitido", HttpStatus.FORBIDDEN);
        }

       if(!loan.getPayments().contains(paymentLoan)){
           return new ResponseEntity<>("La cantidad de cuotas no corresponde con el prestamo.", HttpStatus.FORBIDDEN);
       }

       if (client.getLoans().stream().filter(loanF -> loanF.getId().equals(idLoan)).count() > 0){
           Movement newMovement = new Movement("Solicitud de prestamo rechazada. Motivo: No es posible tener mas de 1 prestamo " + loan.getName(), LocalDateTime.now(), client);
           movementService.save(newMovement);

           return new ResponseEntity<>("No es posible enviar la solicitud. Ud. ya posee un prestamo" + " "+ loan.getName(), HttpStatus.FORBIDDEN);
       }

       ClientLoan newClientLoan = new ClientLoan(amountLoan, paymentLoan, loan, client);
       clientLoanRepository.save(newClientLoan);


        Transaction newTransactionC = new Transaction(TransactionType.CREDITO, amountLoan, "El prestamo "+ loan.getName()+" fue aprobado"  , LocalDateTime.now());
        transactionService.save(newTransactionC);
        accountTo.addTransaction(newTransactionC);

        double newBalanceD = accountTo.getBalance() + amountLoan;
        accountTo.setBalance(newBalanceD);
        accountService.save(accountTo);

        Movement newMovement = new Movement("El prestamo "+ loan.getName() + " fue aprobado. El monto solicitado ya se encuentra disponible en la cuenta ' " + loanApplicationDTO.getAccountTo() + " '", LocalDateTime.now(), client);
        movementService.save(newMovement);

       return new ResponseEntity<>("Solicitud de préstamo exitosa", HttpStatus.CREATED);

   }

}
