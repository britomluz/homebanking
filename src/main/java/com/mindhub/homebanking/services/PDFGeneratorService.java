package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface PDFGeneratorService {
    public void exportAccount(HttpServletResponse response, List<AccountDTO> listAccounts, Client client) throws IOException;
    public void exportTransfer(HttpServletResponse response, Transaction transaction, Account account, Client client) throws IOException;
    public void exportTransaction(HttpServletResponse response, Client client, String amount, String description, String originNumberAccount, String destinyNumberAccount) throws IOException;
    public void exportListTransactions(HttpServletResponse response, List<Transaction> transactions, Client client, LocalDateTime from, LocalDateTime to, Account account) throws IOException;

}


