package com.mindhub.homebanking.services.impl;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Transaction save(Transaction transaction) {

        return transactionRepository.save(transaction);
    }

    @Override
    public Optional<Transaction> getById(Long id){

        return transactionRepository.findById(id);
    }

    @Override
    public List<Transaction> getAllByDateBetween(LocalDateTime from, LocalDateTime to) {
        return transactionRepository.findAllByDateBetween(from,to);
        //SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        //Date fromm = formater.parse(from);
        //Date too = formater.parse(to);

        //System.out.println("desde = "+fromm+" hastaa = "+too);
    }

    @Override
    public void deleteAllTransactions(Set<Transaction> transactions){
        transactionRepository.deleteAll(transactions);
    }


}
